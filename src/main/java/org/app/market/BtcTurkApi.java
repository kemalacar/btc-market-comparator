package org.app.market;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.app.CoinRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kemal Acar
 */
public class BtcTurkApi extends BaseApi {

    public BtcTurkApi(CoinRepository calculator) {
        super(calculator);
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
    String getSocketUrl() {
        return "wss://ws-feed-pro.btcturk.com";
    }
}
