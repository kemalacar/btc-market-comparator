package org.app.market.paribu;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.app.market.BaseCoinApi;
import org.app.market.Market;
import org.app.market.MarketPriceCacheContext;
import org.app.repository.CoinRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kemal Acar
 */
public class ParibuApi extends BaseCoinApi {

    private String coinName;
    private List<CoinRepository.Param> recordList = new ArrayList<>(); // todo thread safe CopyOnwrite olacak bakılmalı

    public ParibuApi(CoinRepository calculator) {
        super(calculator);
    }

    @Override
    protected String getSocketUrl() {
        return "wss://ws-eu.pusher.com/app/9583280bf9e54779ac66?protocol=7&client=js&version=7.6.0&flash=false";
    }

    @Override
    public void onMessage(String msg) {
        try {
            CoinRepository.Param param = parseResponse(msg);
            if (param != null && param.bid != null) {
                save(param);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void save(CoinRepository.Param param) {
        recordList.add(param);
        if (recordList.size() > 9) {
            coinRepository.saveParamToDb(recordList);
            recordList = new ArrayList<>();
        }
    }

    @Override
    public void subscribe(String coinName) {
        this.coinName = coinName;
        String message = "{\"event\": \"pusher:subscribe\",\"data\": {\"auth\": \"\",\"channel\": \"cache-market-" + coinName + "-orderbook\"}}";
        client.send(message);
    }

    public CoinRepository.Param parseResponse(String responseString) throws JsonProcessingException {
        try {
            CoinRepository.Param param = new CoinRepository.Param();
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

    private void setDifferencesWithMarketPrice(CoinRepository.Param param, MarketEvent.Data data) {
        CoinRepository.MainMarketCoin lastMainMarketCoin = MarketPriceCacheContext.mainMarketCoins.peek();
        param.marketEventTime = lastMainMarketCoin.eventTime;
        param.marketPrice = lastMainMarketCoin.price;
        param.marketDifferencePercentage = ((param.marketPrice - Double.parseDouble(data.getPayload().getSell().keySet().stream().findFirst().orElse("0"))) / param.marketPrice) * 100;
    }
}
