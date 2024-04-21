package org.app.repository;

import org.app.market.Market;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Kemal Acar
 */
public class CoinRepository {

    private final Database database;

    public CoinRepository(Database database) {
        this.database = database;
    }

    public void saveParamToDb(Param param) {
        CompletableFuture.runAsync(() -> database.saveDbToCoin(param, param.market + "_" + param.coin));
    }

    public void saveMainMarketCoin(MainMarketCoin mainMarketCoin) {
        CompletableFuture.runAsync(() -> database.saveMarketPrice(mainMarketCoin, mainMarketCoin.market + "_" + mainMarketCoin.coin));
    }

    public static class Param {
        public Market market;
        public long dateTime;
        public String coin;
        public Map<String,String> bid;
        public Map<String,String> ask;
    }

    public static class MainMarketCoin {
        public Market market;
        public String eventTime;
        public long saveTime; // Our system save time
        public String coin;
        public String price;
        public String quantity;
    }
}

