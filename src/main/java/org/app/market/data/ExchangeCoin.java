package org.app.market.data;

import java.util.Map;

/**
 * @author anercan
 */
public class ExchangeCoin {
    public Market market;
    public long dateTime;
    public String coin;
    public Map<String, String> bid;
    public Map<String, String> ask;
    public double marketPrice;
    public String marketEventTime;
    public double marketDifferencePercentage;
}
