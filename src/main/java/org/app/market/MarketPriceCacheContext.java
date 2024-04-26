package org.app.market;

import org.app.repository.CoinRepository;

/**
 * @author anercan
 */
public class MarketPriceCacheContext {

    public static CoinRepository.MainMarketCoin mainMarketCoin = new CoinRepository.MainMarketCoin();

    public static void setMainMarketCoin(CoinRepository.MainMarketCoin mainMarketCoin) {
        MarketPriceCacheContext.mainMarketCoin = mainMarketCoin;
    }
}
