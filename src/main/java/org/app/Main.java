package org.app;

import org.app.market.ParibuApi;

/**
 * @author Kemal Acar
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Server started...");

        CoinRepository coinRepository = new CoinRepository(new Database());

        new ParibuApi(coinRepository).connect().subscribe("btc_usdt");
        //new BtcTurkApi(coinRepository).connect().subscribe("BTCUSDT");
        //new BinanceApi(coinRepository).subscribe("btcusdt");
    }

}

