package org.app;

import org.app.market.binance.BinanceApi;
import org.app.market.paribu.ParibuApi;
import org.app.repository.CoinRepository;
import org.app.repository.Database;

/**
 * @author Kemal Acar
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Server started...");

        CoinRepository coinRepository = new CoinRepository(new Database());

        new ParibuApi(coinRepository).connect().subscribe("btc_usdt");
        //new BtcTurkApi(coinRepository).connect().subscribe("BTCUSDT");
        new BinanceApi(coinRepository).subscribe("btcusdt");
    }

}

