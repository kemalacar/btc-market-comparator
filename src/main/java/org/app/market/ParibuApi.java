package org.app.market;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.app.Calculator;
import org.app.JavaWebSocketClient;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;

/**
 * @author Kemal Acar
 */
public class ParibuApi extends BaseApi {
    private JavaWebSocketClient client;

    public ParibuApi(Calculator calculator) {
        super(calculator);
    }

    public ParibuApi connect() {

        try {
            client = new JavaWebSocketClient(msg -> {
                try {
                    Calculator.Param param = new Calculator.Param();
                    param.paribuPrice = parsePrice(msg);
                    if (param.paribuPrice != null) {
                        calculator.calculate(param);
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }, new URI("wss://ws-eu.pusher.com/app/9583280bf9e54779ac66?protocol=7&client=js&version=7.6.0&flash=false"));
            client.connectBlocking();
            Thread.sleep(3000);
        } catch (URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public ParibuApi subscribe() {
        String message = "{\"event\": \"pusher:subscribe\",\"data\": {\"auth\": \"\",\"channel\": \"cache-market-btc_usdt-latest-matches\"}}";
        client.send(message);
        return this;
    }

    public BigDecimal parsePrice(String response) throws JsonProcessingException {
        HashMap map = objectMapper.readValue(response, HashMap.class);
        Object event = map.get("event");
        if ("diff".equals(event)) {
            Object data = map.get("data");
            ParibuResponse res = objectMapper.readValue(data.toString(), ParibuResponse.class);
            Comparator<ParibuResponse.Item> employeeAgeComparator = Comparator
                    .comparing(ParibuResponse.Item::getTimestamp);
            Optional<ParibuResponse.Item> max = res.payload.values().stream()
                    .max(employeeAgeComparator);
            return BigDecimal.valueOf(max.get().price);
        }
        return null;
    }


    public static class ParibuResponse {
        public String action;
        public HashMap<String, Item> payload = new HashMap<>();

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
