package org.app;

import org.app.market.Market;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        CompletableFuture.runAsync(() -> database.saveToDb(param, param.market + "_" + param.coin));
    }

    public static class Param {
        public Market market;
        public LocalDateTime dateTime = LocalDateTime.now();
        public String coin;
        public double amount;
        public BigDecimal price;
    }
}

