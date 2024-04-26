package org.app;

import org.app.market.Coin;
import org.app.market.Market;
import org.app.market.MarketUtil;
import org.app.market.binance.BinanceApi;
import org.app.market.paribu.ParibuApi;
import org.app.repository.CoinRepository;
import org.app.repository.Database;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Kemal Acar
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Server started...");
        CoinRepository coinRepository = new CoinRepository(new Database());

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        HashMap<Market, String> marketCoinMap = MarketUtil.coinNamesInMarketsMap.get(Coin.AVAX);

        //Main Market Coin
        executorService.submit(() -> new BinanceApi(coinRepository).subscribe(marketCoinMap.get(Market.BINANCE)));
        //Other Exchanges
        executorService.submit(() -> new ParibuApi(coinRepository).connect().subscribe(marketCoinMap.get(Market.PARIBU)));
        //new BtcTurkApi(coinRepository).connect().subscribe(marketCoinMap.get(Market.BTCTURK));
    }

}

