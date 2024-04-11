package org.app;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Kemal Acar
 */
public class Calculator {

    private final Param lastValues = new Param();

    public void calculate(Param param) {
        if (param.binancePrice != null) lastValues.binancePrice = param.binancePrice;
        if (param.btcTurkPrice != null) lastValues.btcTurkPrice = param.btcTurkPrice;
        if (param.paribuPrice != null) lastValues.paribuPrice = param.paribuPrice;


        System.out.println("--------------------------------------------");
        System.out.println("BTC TURK\t: => " + lastValues.btcTurkPrice + "\t\t=> " + (lastValues.btcTurkPrice != null ? lastValues.btcTurkPrice.subtract(lastValues.binancePrice).setScale(2, RoundingMode.UP) : 0) + "$");
        System.out.println("PRB \t\t: => " + lastValues.paribuPrice + "\t\t=> " + (lastValues.paribuPrice != null ? lastValues.paribuPrice.subtract(lastValues.binancePrice).setScale(2, RoundingMode.UP) : 0) + "$");
        System.out.println("BINANCE \t: => " + lastValues.binancePrice);

    }

    public Param getLastValues() {
        return lastValues;
    }


    public static class Param {
        public BigDecimal binancePrice = BigDecimal.ZERO,
                paribuPrice,
                btcTurkPrice;

        @Override
        public String toString() {
            return "Param{" +
                    "binancePrice=" + binancePrice +
                    ", paribuPrice=" + paribuPrice +
                    ", btcTurkPrice=" + btcTurkPrice +
                    '}';
        }
    }
}

