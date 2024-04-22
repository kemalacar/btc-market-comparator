package org.app.market;

import java.util.HashMap;

/**
 * @author anercan
 */
public class MarketUtil {

    public static HashMap<Coin,HashMap<Market,String>> coinNamesInMarketsMap = new HashMap<>();

    static {
        HashMap<Market, String> coinNameInMarket = new HashMap<>();
        coinNameInMarket.put(Market.BINANCE,"btcusdt");
        coinNameInMarket.put(Market.PARIBU,"btc_usdt");
        coinNameInMarket.put(Market.BTCTURK,"BTCUSDT");

        coinNamesInMarketsMap.put(Coin.BTC, coinNameInMarket);
    }

}
