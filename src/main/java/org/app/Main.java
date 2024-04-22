package org.app;

import org.app.market.Coin;
import org.app.market.Market;
import org.app.market.MarketUtil;
import org.app.market.binance.BinanceApi;
import org.app.market.paribu.ParibuApi;
import org.app.repository.CoinRepository;
import org.app.repository.Database;

import java.util.HashMap;

/**
 * @author Kemal Acar
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Server started...");
        CoinRepository coinRepository = new CoinRepository(new Database());

        HashMap<Market, String> marketCoinMap = MarketUtil.coinNamesInMarketsMap.get(Coin.BTC);

        //Main Market Coin
        new BinanceApi(coinRepository).subscribe(marketCoinMap.get(Market.BINANCE));

        Thread.sleep(1000l);

        //Other Exchanges
        new ParibuApi(coinRepository).connect().subscribe(marketCoinMap.get(Market.PARIBU));
        //new BtcTurkApi(coinRepository).connect().subscribe(marketCoinMap.get(Market.BTCTURK));
    }

}

