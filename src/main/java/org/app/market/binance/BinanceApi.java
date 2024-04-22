package org.app.market.binance;

import com.binance.connector.client.WebSocketStreamClient;
import com.binance.connector.client.impl.WebSocketStreamClientImpl;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import org.app.market.BaseApi;
import org.app.market.Market;
import org.app.market.MarketPriceCacheContext;
import org.app.repository.CoinRepository;

import java.io.IOException;

/**
 * @author Kemal Acar
 */
public class BinanceApi extends BaseApi {
    public static final String PRICE = "p";
    public static final String EVENT_TIME = "E";
    public static final String QUANTITY = "q";
    private final WebSocketStreamClient client;
    private final JsonFactory factory;

    public BinanceApi(CoinRepository calculator) {
        super(calculator);
        client = new WebSocketStreamClientImpl();
        factory = new JsonFactory();
    }

    public void subscribe(String coinName) {
        client.tradeStream(coinName, message -> {
            TradeEvent tradeEvent = getTradeEvent(message);
            if (tradeEvent != null) {
                CoinRepository.MainMarketCoin mainMarketCoin = new CoinRepository.MainMarketCoin();
                mainMarketCoin.saveTime = System.currentTimeMillis();
                mainMarketCoin.market = Market.BINANCE;
                mainMarketCoin.coin = coinName;
                mainMarketCoin.price = tradeEvent.price;
                mainMarketCoin.quantity = tradeEvent.quantity;
                mainMarketCoin.eventTime = tradeEvent.eventTime;

                save(mainMarketCoin);
            }
        });
    }

    private void save(CoinRepository.MainMarketCoin mainMarketCoin) {
        MarketPriceCacheContext.mainMarketCoins.add(mainMarketCoin);
        if (MarketPriceCacheContext.mainMarketCoins.size() > 20) { // for bulk save
            //coinRepository.saveMainMarketCoin(MarketPriceCacheContext.mainMarketCoins);
            MarketPriceCacheContext.mainMarketCoins.clear();
            MarketPriceCacheContext.mainMarketCoins.add(mainMarketCoin);
        }
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

