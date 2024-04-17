package org.app.market;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.app.CoinRepository;

import java.util.Map;

/**
 * @author Kemal Acar
 */
public class ParibuApi extends BaseApi {

    private String coinName;

    public ParibuApi(CoinRepository calculator) {
        super(calculator);
    }

    @Override
    String getSocketUrl() {
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
        CoinRepository.Param param = new CoinRepository.Param();
        ParibuResponse response = objectMapper.readValue(responseString, ParibuResponse.class); //todo parser hatası
        ParibuResponse.Data data = response.data;
        param.market = Market.PARIBU;
        param.bid = data.payload.buy;
        param.ask = data.payload.sell;
        param.coin = this.coinName;
        param.dateTime = System.currentTimeMillis();
        return param;
    }

    public static class ParibuResponse {
        public Data data;

        @JsonCreator
        public ParibuResponse(@JsonProperty Data data) {
            this.data = data;
        }
        public static class Data {
            @JsonCreator
            public Data(@JsonProperty Payload payload) {
                this.payload = payload;
            }
            private Payload payload;
        }
        public static class Payload {
            @JsonCreator
            public Payload(@JsonProperty("buy") Map<String, String> buy, @JsonProperty("sell") Map<String, String> sell) {
                this.buy = buy;
                this.sell = sell;
            }
            private Map<String, String> buy;
            private Map<String, String> sell;
        }
    }
}
