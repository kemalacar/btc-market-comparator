package org.app.market;

import com.binance.connector.client.WebSocketStreamClient;
import com.binance.connector.client.impl.WebSocketStreamClientImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.app.Calculator;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @author Kemal Acar
 */
public class BinanceApi extends BaseApi {
    private final WebSocketStreamClient client = new WebSocketStreamClientImpl();

    public BinanceApi(Calculator calculator) {
        super(calculator);
    }

    public void subscribe() {
        client.tradeStream("btcusdt", message -> {
            try {
                HashMap map = objectMapper.readValue(message, HashMap.class);
                Calculator.Param param = new Calculator.Param();
                param.binancePrice = BigDecimal.valueOf(Double.parseDouble(map.get("p").toString()));
                calculator.calculate(param);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

}

