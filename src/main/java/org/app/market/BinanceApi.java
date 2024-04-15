package org.app.market;

import com.binance.connector.client.WebSocketStreamClient;
import com.binance.connector.client.impl.WebSocketStreamClientImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.app.CoinRepository;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @author Kemal Acar
 */
public class BinanceApi extends BaseApi {
    private final WebSocketStreamClient client = new WebSocketStreamClientImpl();

    public BinanceApi(CoinRepository calculator) {
        super(calculator);
    }

    public void subscribe() {
        client.tradeStream("btcusdt", message -> {
            try {
                HashMap map = objectMapper.readValue(message, HashMap.class);
                CoinRepository.Param param = new CoinRepository.Param();
                param.price = BigDecimal.valueOf(Double.parseDouble(map.get("p").toString()));
                if (param.price.compareTo(BigDecimal.ZERO) > 0) {
                    param.market = Market.BINANCE;
                    param.coin = "BTC";
                    coinRepository.saveParamToDb(param);
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

}

