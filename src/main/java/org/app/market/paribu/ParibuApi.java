package org.app.market.paribu;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.app.market.BaseCoinApi;
import org.app.market.Market;
import org.app.repository.CoinRepository;

/**
 * @author Kemal Acar
 */
public class ParibuApi extends BaseCoinApi {

    private String coinName;

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
                coinRepository.saveParamToDb(param);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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
            param.bid = data.getPayload().getSell();
            param.ask = data.getPayload().getBuy();
            param.coin = this.coinName;
            param.dateTime = System.currentTimeMillis();
            return param;
        } catch (Exception e) {
            return null;
        }
    }
}
