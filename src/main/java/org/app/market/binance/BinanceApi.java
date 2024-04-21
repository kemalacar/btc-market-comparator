package org.app.market.binance;

import com.binance.connector.client.WebSocketStreamClient;
import com.binance.connector.client.impl.WebSocketStreamClientImpl;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import org.app.market.BaseApi;
import org.app.market.Market;
import org.app.repository.CoinRepository;

import java.io.IOException;

/**
 * @author Kemal Acar
 */
public class BinanceApi extends BaseApi {
    public static final String PRICE = "p";
    public static final String EVENT_TIME = "E";
    public static final String QUANTITY = "q";
    private final WebSocketStreamClient client = new WebSocketStreamClientImpl();
    JsonFactory factory = new JsonFactory();

    public BinanceApi(CoinRepository calculator) {
        super(calculator);
    }

    public void subscribe(String coinName) {
        client.tradeStream(coinName, message -> {
            TradeEvent tradeEvent = getTradeEvent(message);
            CoinRepository.MainMarketCoin mainMarketCoin = new CoinRepository.MainMarketCoin();
            if (tradeEvent != null) {
                mainMarketCoin.market = Market.BINANCE;
                mainMarketCoin.coin = coinName;
                mainMarketCoin.price = tradeEvent.price;
                mainMarketCoin.quantity = tradeEvent.quantity;
                mainMarketCoin.eventTime = tradeEvent.eventTime;
                mainMarketCoin.saveTime = System.currentTimeMillis();

                coinRepository.saveMainMarketCoin(mainMarketCoin);
            }
        });
    }

    private TradeEvent getTradeEvent(String message) {
        try {
            JsonParser parser = factory.createParser(message);

            TradeEvent tradeEvent = new TradeEvent();
            while (!parser.isClosed()) {
                String fieldName = parser.nextFieldName();
                if (fieldName == null) {
                    continue;
                }
                switch (fieldName) {
                    case PRICE -> {
                        parser.nextToken();
                        tradeEvent.price = parser.getText();
                    }
                    case EVENT_TIME -> {
                        parser.nextToken();
                        tradeEvent.eventTime = parser.getText();
                    }
                    case QUANTITY -> {
                        parser.nextToken();
                        tradeEvent.quantity = parser.getText();
                        parser.close();
                        return tradeEvent;
                    }
                }
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    private static class TradeEvent {
        String eventTime;
        String price;
        String quantity;
    }


}

