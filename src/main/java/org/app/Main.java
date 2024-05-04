package org.app;

import org.app.config.AppConfig;
import org.app.market.data.Coin;

/**
 * @author Kemal Acar
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Server started...");
        AppConfig.startApplication(Coin.BTC, true);
    }

}

