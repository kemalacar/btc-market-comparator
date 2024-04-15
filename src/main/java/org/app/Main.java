package org.app;

import org.app.market.BtcTurkApi;

/**
 * @author Kemal Acar
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Server started...");

        CoinRepository coinRepository = new CoinRepository(new Database());

        //new ParibuApi(coinRepository).connect().subscribe();
        new BtcTurkApi(coinRepository).connect().subscribe();
        //new BinanceApi(coinRepository).subscribe();
    }

}

