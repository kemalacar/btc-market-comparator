package org.app.market;

import java.util.HashMap;

/**
 * @author anercan
 */
public class MarketUtil {

    public static HashMap<Coin, HashMap<Market, String>> coinNamesInMarketsMap = new HashMap<>();

    static {
        HashMap<Market, String> coinNameInMarket = new HashMap<>();
        coinNameInMarket.put(Market.BINANCE, "btcusdt");
        coinNameInMarket.put(Market.PARIBU, "btc_usdt");
        coinNameInMarket.put(Market.BTCTURK, "BTCUSDT");
        coinNamesInMarketsMap.put(Coin.BTC, coinNameInMarket);

        coinNameInMarket.put(Market.BINANCE, "avaxusdt");
        coinNameInMarket.put(Market.PARIBU, "avax_usdt");
        //coinNameInMarket.put(Market.BTCTURK,);
        coinNamesInMarketsMap.put(Coin.AVAX, coinNameInMarket);

        coinNameInMarket.put(Market.BINANCE, "xlmusdt");
        coinNameInMarket.put(Market.PARIBU, "xlm_usdt");
        //coinNameInMarket.put(Market.BTCTURK,);
        coinNamesInMarketsMap.put(Coin.STELLAR, coinNameInMarket);
    }

}
