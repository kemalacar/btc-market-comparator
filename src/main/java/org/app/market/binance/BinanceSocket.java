package org.app.market.binance;

import com.binance.connector.client.WebSocketStreamClient;
import com.binance.connector.client.impl.WebSocketStreamClientImpl;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import org.app.market.MarketPriceCacheContext;
import org.app.market.data.MainMarketCoin;
import org.app.market.data.Market;

import java.io.IOException;

/**
 * @author Kemal Acar
 */
public class BinanceSocket {
    public static final String PRICE = "p";
    public static final String EVENT_TIME = "E";
    public static final String QUANTITY = "q";
    private final WebSocketStreamClient client;
    private final JsonFactory factory;

    public BinanceSocket() {
        client = new WebSocketStreamClientImpl();
        factory = new JsonFactory();
    }

    public void subscribe(String coinName) {
        //todo orderbook bid - ask farkları
        client.tradeStream(coinName, message -> {
            TradeEvent tradeEvent = getTradeEvent(message);
            if (tradeEvent != null) {
                MainMarketCoin mainMarketCoin = new MainMarketCoin();
                mainMarketCoin.saveTime = System.currentTimeMillis();
                mainMarketCoin.market = Market.BINANCE;
                mainMarketCoin.coin = coinName;
                mainMarketCoin.price = tradeEvent.price; //highest bid in binance
                mainMarketCoin.quantity = tradeEvent.quantity;
                mainMarketCoin.eventTime = tradeEvent.eventTime;

                save(mainMarketCoin);
            }
        });
    }

    private void save(MainMarketCoin mainMarketCoin) {
        MarketPriceCacheContext.setMainMarketCoin(mainMarketCoin);
        System.out.println(mainMarketCoin.market + "-" + mainMarketCoin.coin + " price:" + mainMarketCoin.price);
        //coinRepository.saveMainMarketCoin();
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
                        tradeEvent.price = Double.parseDouble(parser.getText());
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
        double price;
        String quantity;
    }
}

