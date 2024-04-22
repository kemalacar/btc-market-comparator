package org.app.repository;

import org.app.market.Market;

import java.util.List;
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

    public void saveParamToDb(List<Param> paramList) {
        CompletableFuture.runAsync(() -> database.saveDbToCoinList(paramList, paramList.get(0).market + "_" + paramList.get(0).coin));
    }

    public void saveMainMarketCoin(List<MainMarketCoin> mainMarketCoin) {
        CompletableFuture.runAsync(() -> database.saveMarketPrice(mainMarketCoin, mainMarketCoin.get(0).market + "_" + mainMarketCoin.get(0).coin));
    }

    public static class Param {
        public Market market;
        public long dateTime;
        public String coin;
        public Map<String,String> bid;
        public Map<String,String> ask;
        public double marketPrice;
        public String marketEventTime;
        public double marketDifferencePercentage;
    }

    public static class MainMarketCoin {
        public Market market;
        public String eventTime;
        public long saveTime; // Our system save time
        public String coin;
        public double price;
        public String quantity;
    }
}

