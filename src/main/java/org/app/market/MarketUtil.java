package org.app.market;

import org.app.market.data.Coin;
import org.app.market.data.Market;

import java.util.HashMap;

/**
 * @author anercan
 */
public class MarketUtil {

    public static HashMap<Coin, HashMap<Market, String>> coinNamesInMarketsMap = new HashMap<>();

    static {
        coinNamesInMarketsMap.put(Coin.BTC, btcMarketMap());
        coinNamesInMarketsMap.put(Coin.AVAX, avaxMarketMap());
        coinNamesInMarketsMap.put(Coin.STELLAR, stellarMarketMap());
        coinNamesInMarketsMap.put(Coin.WORMHOLE, wormholeMarketMap());
    }

    private static HashMap<Market, String> btcMarketMap() {
        HashMap<Market, String> coinNameInMarket = new HashMap<>();
        coinNameInMarket.put(Market.BINANCE, "btcusdt");
        coinNameInMarket.put(Market.PARIBU, "btc_usdt");
        coinNameInMarket.put(Market.BTCTURK, "BTCUSDT");
        return coinNameInMarket;
    }

    private static HashMap<Market, String> avaxMarketMap() {
        HashMap<Market, String> coinNameInMarket = new HashMap<>();
        coinNameInMarket.put(Market.BINANCE, "avaxusdt");
        coinNameInMarket.put(Market.PARIBU, "avax_usdt");
        //coinNameInMarket.put(Market.BTCTURK,);
        return coinNameInMarket;
    }

    private static HashMap<Market, String> stellarMarketMap() {
        HashMap<Market, String> coinNameInMarket = new HashMap<>();
        coinNameInMarket.put(Market.BINANCE, "xlmusdt");
        coinNameInMarket.put(Market.PARIBU, "xlm_usdt");
        //coinNameInMarket.put(Market.BTCTURK,);
        return coinNameInMarket;
    }

    private static HashMap<Market, String> wormholeMarketMap() {
        HashMap<Market, String> coinNameInMarket = new HashMap<>();
        coinNameInMarket.put(Market.BINANCE, "wtry");
        coinNameInMarket.put(Market.PARIBU, "w_tl");
        //coinNameInMarket.put(Market.BTCTURK,);
        return coinNameInMarket;
    }

}
