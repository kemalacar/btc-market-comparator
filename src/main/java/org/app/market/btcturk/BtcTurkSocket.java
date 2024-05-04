package org.app.market.btcturk;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.app.market.BaseSocket;
import org.app.market.data.ExchangeCoin;
import org.app.repository.CoinRepository;
import org.app.wallet.WalletOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Kemal Acar
 */
public class BtcTurkSocket extends BaseSocket {

    private String coinName;
    private WalletOperation walletOperation;
    private final CopyOnWriteArrayList<ExchangeCoin> recordList = new CopyOnWriteArrayList<>();
    private boolean performTrade;

    public BtcTurkSocket(CoinRepository coinRepository, WalletOperation walletOperation,boolean performTrade) {
        super(coinRepository);
        this.walletOperation = walletOperation;
        this.performTrade = performTrade;
    }

    @Override
    public void onMessage(String msg) {
        try {
            List<?> arrays = objectMapper.readValue(msg, ArrayList.class);
            if ((int) arrays.get(0) == 422) {
                /*Map<String, String> map = (HashMap<String, String>) arrays.get(1);
                CoinRepository.Param param = new CoinRepository.Param();
                param.price = BigDecimal.valueOf(Double.parseDouble(map.get("P")));
                param.market = Market.BTCTURK;
                param.coin = "BTC";
                param.amount = Double.parseDouble(map.get("A"));
                coinRepository.saveParamToDb(param);*/
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void subscribe(String coinName) {
        Object[] message = new Object[2];
        message[0] = 151;
        message[1] = "{\"type\":151,\"channel\":\"trade\",\"event\":\"" + coinName + "\",\"join\":true}";
        client.send(Arrays.toString(message));
    }

    @Override
    protected String getSocketUrl() {
        return "wss://ws-feed-pro.btcturk.com";
    }
}
