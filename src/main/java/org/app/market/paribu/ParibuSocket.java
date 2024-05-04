package org.app.market.paribu;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.app.market.BaseSocket;
import org.app.market.MarketPriceCacheContext;
import org.app.market.data.ExchangeCoin;
import org.app.market.data.MainMarketCoin;
import org.app.market.data.Market;
import org.app.repository.CoinRepository;
import org.app.wallet.WalletOperation;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Kemal Acar
 */
public class ParibuSocket extends BaseSocket {

    private String coinName;
    private WalletOperation walletOperation;
    private final CopyOnWriteArrayList<ExchangeCoin> recordList = new CopyOnWriteArrayList<>();
    private boolean performTrade;

    public ParibuSocket(CoinRepository coinRepository, WalletOperation walletOperation, boolean performTrade) {
        super(coinRepository);
        this.walletOperation = walletOperation;
        this.performTrade = performTrade;
    }

    @Override
    protected String getSocketUrl() {
        return "wss://ws-eu.pusher.com/app/9583280bf9e54779ac66?protocol=7&client=js&version=7.6.0&flash=false";
    }

    @Override
    public void onMessage(String msg) {
        try {
            ExchangeCoin param = parseResponse(msg);
            if (param != null && param.bid != null) {
                save(param);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void save(ExchangeCoin param) {
        recordList.add(param);
        System.out.println(param.market + " Trade Event - Market Price:" + param.marketPrice + " Trade Time:" + param.dateTime);
        if (recordList.size() > 9) {
            coinRepository.saveParamToDb(recordList);
        }
    }

    @Override
    public void subscribe(String coinName) {
        this.coinName = coinName;
        String message = "{\"event\": \"pusher:subscribe\",\"data\": {\"auth\": \"\",\"channel\": \"cache-market-" + coinName + "-orderbook\"}}";
        client.send(message);
    }

    public ExchangeCoin parseResponse(String responseString) throws JsonProcessingException {
        try {
            ExchangeCoin param = new ExchangeCoin();
            MarketEvent response = objectMapper.readValue(responseString, MarketEvent.class);
            MarketEvent.Data data = response.getData();
            param.market = Market.PARIBU;
            param.bid = data.getPayload().getBuy();
            param.ask = data.getPayload().getSell();
            param.coin = this.coinName;
            param.dateTime = System.currentTimeMillis();
            setDifferencesWithMarketPrice(param, data);
            return param;
        } catch (Exception e) {
            return null;
        }
    }

    private void setDifferencesWithMarketPrice(ExchangeCoin param, MarketEvent.Data data) {
        MainMarketCoin lastMainMarketCoin = MarketPriceCacheContext.mainMarketCoin;
        if (lastMainMarketCoin.market == null) {
            throw new RuntimeException("lastMainMarketCoin is null");
        }
        param.marketEventTime = lastMainMarketCoin.eventTime;
        param.marketPrice = lastMainMarketCoin.price;
        double lowestAskPrice = Double.parseDouble(data.getPayload().getSell().keySet().stream().findFirst().orElse("0"));
        param.marketDifferencePercentage = ((param.marketPrice - lowestAskPrice) / param.marketPrice) * 100;

        if (performTrade) {
            if (walletOperation.getMoneyAmount() > 0 && param.marketDifferencePercentage > 0.5d) {
                walletOperation.buyCoin(param);
            }
            if (walletOperation.getQuantity() != 0 && ((lowestAskPrice - walletOperation.getFrom()) / walletOperation.getFrom()) > 0.5) {
                walletOperation.sellCoin(lowestAskPrice);
            }
        }

    }
}
