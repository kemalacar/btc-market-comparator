package org.app.market;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.app.CoinRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;

/**
 * @author Kemal Acar
 */
public class ParibuApi extends BaseApi {
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
            if (param != null && param.price != null) {
                coinRepository.saveParamToDb(param);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void subscribe() {
        String message = "{\"event\": \"pusher:subscribe\",\"data\": {\"auth\": \"\",\"channel\": \"cache-market-btc_usdt-latest-matches\"}}";
        client.send(message);
    }

    public CoinRepository.Param parseResponse(String response) throws JsonProcessingException {
        CoinRepository.Param param = new CoinRepository.Param();
        HashMap map = objectMapper.readValue(response, HashMap.class);
        Object event = map.get("event");
        if ("diff".equals(event)) {
            Object data = map.get("data");
            ParibuResponse res = objectMapper.readValue(data.toString(), ParibuResponse.class);
            Comparator<ParibuResponse.Item> latestComparator = Comparator
                    .comparing(ParibuResponse.Item::getTimestamp);
            Optional<ParibuResponse.Item> max = res.payload.values().stream()
                    .max(latestComparator);

            ParibuResponse.Item item = max.orElse(null);
            if (item != null) {
                param.price = BigDecimal.valueOf(item.price);
                param.market = Market.PARIBU;
                param.coin = "BTC";
                param.amount = item.amount;
                return param;
            }
        }
        return null;
    }


    public static class ParibuResponse {
        public HashMap<String, Item> payload = new HashMap<>();
        public String action;

        public static class Item {
            public Double amount;
            public Double price;
            public LocalDateTime timestamp;
            public String trade;

            public LocalDateTime getTimestamp() {
                return timestamp;
            }
        }
    }
}
