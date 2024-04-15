package org.app;

import org.app.market.Market;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * @author Kemal Acar
 */
public class Calculator {

    private final Database database;

    public Calculator(Database database) {
        this.database = database;
    }

    private final LastValue lastValue = new LastValue();


    public void calculate(Param param) {
        switch (param.market) {
            case BINANCE -> lastValue.binancePrice = param.price;
            case BTCTURK -> lastValue.btcTurkPrice = param.price;
            case PARIBU -> lastValue.paribuPrice = param.price;
        }

        try {
            new ProcessBuilder("clear").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

//        database.saveToDb(param);

        System.out.println("--------------------------------------------");
        System.out.println("BTC TURK\t: => " + lastValue.btcTurkPrice + "\t\t=> " + (lastValue.btcTurkPrice != null ? lastValue.btcTurkPrice.subtract(lastValue.binancePrice).setScale(2, RoundingMode.UP) : 0) + "$");
        System.out.println("PRB \t\t: => " + lastValue.paribuPrice + "\t\t=> " + (lastValue.paribuPrice != null ? lastValue.paribuPrice.subtract(lastValue.binancePrice).setScale(2, RoundingMode.UP) : 0) + "$");
        System.out.println("BINANCE \t: => " + lastValue.binancePrice);

    }



    public LastValue getLastValue() {
        return lastValue;
    }

    public static class LastValue {
        BigDecimal binancePrice = BigDecimal.ZERO;
        BigDecimal btcTurkPrice;
        BigDecimal paribuPrice;
    }

    public static class Param {
        public Market market;
        public LocalDateTime dateTime = LocalDateTime.now();
        public String coin;
        public BigDecimal price;
    }
}

