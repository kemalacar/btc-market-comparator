package org.app.config;

import org.app.market.MarketUtil;
import org.app.market.binance.BinanceSocket;
import org.app.market.data.Coin;
import org.app.market.data.Market;
import org.app.market.paribu.ParibuSocket;
import org.app.repository.CoinRepository;
import org.app.repository.Database;
import org.app.wallet.WalletOperation;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author anercan
 */
public class AppConfig {

    public static void startApplication(Coin selectedCoin, boolean performTrade) {
        HashMap<Market, String> marketCoinMap = MarketUtil.coinNamesInMarketsMap.get(selectedCoin);
        WalletOperation walletOperations = createWalletOperations(selectedCoin, 1000d, Market.PARIBU, performTrade);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        CoinRepository coinRepository = new CoinRepository(new Database());

        //Main Market Coin
        executorService.submit(() -> new BinanceSocket().subscribe(marketCoinMap.get(Market.BINANCE)));
        //Other Exchanges
        executorService.submit(() -> new ParibuSocket(coinRepository, walletOperations, performTrade).connect().subscribe(marketCoinMap.get(Market.PARIBU)));
        //new BtcTurkSocket(coinRepository).connect().subscribe(marketCoinMap.get(Market.BTCTURK));
    }

    private static WalletOperation createWalletOperations(Coin followingCoin, Double totalWalletAmount, Market operationMarket, boolean performTrade) {
        if (performTrade) {
            return new WalletOperation(totalWalletAmount, followingCoin, operationMarket);
        } else {
            return null;
        }
    }

    //Todo usdt - try çevirimi ? -- binance try listing de var
    //todo paribu askda görünen miktar az olması durumu / alırken 2. 3. sıralara bakılabilir

}
