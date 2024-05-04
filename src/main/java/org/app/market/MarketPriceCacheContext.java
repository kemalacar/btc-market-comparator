package org.app.market;

import org.app.market.data.MainMarketCoin;

/**
 * @author anercan
 */
public class MarketPriceCacheContext {

    public static MainMarketCoin mainMarketCoin = new MainMarketCoin(); //todo map yapılıp ortalaması yazılabilir average-MarketPrice

    public static void setMainMarketCoin(MainMarketCoin mainMarketCoin) {
        MarketPriceCacheContext.mainMarketCoin = mainMarketCoin;
    }
}
