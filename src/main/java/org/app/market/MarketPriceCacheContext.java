package org.app.market;

import org.app.repository.CoinRepository;

import java.util.Stack;

/**
 * @author anercan
 */
public class MarketPriceCacheContext {

    public static Stack<CoinRepository.MainMarketCoin> mainMarketCoins = new Stack<>();

}
