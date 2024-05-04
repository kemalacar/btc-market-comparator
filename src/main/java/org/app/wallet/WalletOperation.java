package org.app.wallet;

import org.app.market.data.Coin;
import org.app.market.data.ExchangeCoin;
import org.app.market.data.Market;

/**
 * @author anercan
 */
public class WalletOperation {

    private Double moneyAmount;
    private Double quantity = 0d;
    private Double from = 0d;
    private Coin coin;
    private Market market;

    public WalletOperation(Double moneyAmount, Coin coin,Market market) {
        this.market = market;
        this.moneyAmount = moneyAmount;
        this.coin = coin;
    }
    //todo debug
    public void buyCoin(ExchangeCoin param) {
        try {
            String lowestAskString = param.ask.keySet().stream().findFirst().orElse("0");

            String lowestAskQuantity = param.ask.get(lowestAskString);
            double lowestAskPrice = Double.parseDouble(lowestAskString);
            double priceToPay = Double.parseDouble(lowestAskQuantity) * lowestAskPrice;
            if (priceToPay < moneyAmount) {
                Thread.sleep(900L);
                moneyAmount += moneyAmount - priceToPay;
                quantity += Double.parseDouble(lowestAskQuantity);
                from = lowestAskPrice;
            } else {
                Thread.sleep(900L);
                double buyQuantity = moneyAmount / lowestAskPrice;
                moneyAmount = moneyAmount - (buyQuantity * lowestAskPrice);
                quantity = buyQuantity;
                from = lowestAskPrice;
            }
            printWallet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sellCoin(double lowestAskPrice) {
        moneyAmount += lowestAskPrice * quantity;
        quantity = 0d;
        printWallet();
    }

    private void printWallet() {
        System.out.println("Wallet -> " + moneyAmount + " $" + " - " + quantity + " " + coin);
    }

    public Double getQuantity() {
        return quantity;
    }

    public Double getFrom() {
        return from;
    }

    public Double getMoneyAmount() {
        return moneyAmount;
    }
}
