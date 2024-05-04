package org.app.repository;

import org.app.market.data.ExchangeCoin;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Kemal Acar
 */
public class CoinRepository {

    private final Database database;

    public CoinRepository(Database database) {
        this.database = database;
    }

    public void saveParamToDb(List<ExchangeCoin> paramList) {
        CompletableFuture.runAsync(() -> {
            database.saveDbToCoinList(paramList, paramList.get(0).market + "_" + paramList.get(0).coin);
            paramList.clear();
        });
    }

}

