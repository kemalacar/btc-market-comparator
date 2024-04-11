package org.app.market;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.app.Calculator;
import org.app.JavaWebSocketClient;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author Kemal Acar
 */
public class BtcTurkApi extends BaseApi {
    private JavaWebSocketClient client;

    public BtcTurkApi(Calculator calculator) {
        super(calculator);
    }

    public BtcTurkApi connect() {

        try {
            client = new JavaWebSocketClient(msg -> {
                try {
                    List arrays = objectMapper.readValue(msg, ArrayList.class);
                    if ((int) arrays.get(0) == 422) {
                        Map<String, String> map = (HashMap<String, String>) arrays.get(1);
                        Calculator.Param param = new Calculator.Param();
                        param.btcTurkPrice = BigDecimal.valueOf(Double.parseDouble(map.get("P").toString()));
                        calculator.calculate(param);
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }, new URI("wss://ws-feed-pro.btcturk.com"));
            client.connectBlocking();

        } catch (URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public BtcTurkApi subscribe() {
        Object[] message = new Object[2];
        message[0] = 151;
        message[1] = "{\"type\":151,\"channel\":\"trade\",\"event\":\"BTCUSDT\",\"join\":true}";
        client.send(Arrays.toString(message));
        return this;
    }


}
