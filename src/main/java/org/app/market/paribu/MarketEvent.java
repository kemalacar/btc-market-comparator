package org.app.market.paribu;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class MarketEvent {
    private String event;
    private Data data;
    private String channel;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Data getData() {
        return data;
    }

    public void setData(String data) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.data = objectMapper.readValue(data, Data.class);
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public static class Data {
        private String action;
        private Payload payload;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public Payload getPayload() {
            return payload;
        }

        public void setPayload(Payload payload) {
            this.payload = payload;
        }
    }

    public static class Payload {
        private Map<String, String> buy;
        private Map<String, String> sell;

        public Map<String, String> getBuy() {
            return buy;
        }

        public void setBuy(Map<String, String> buy) {
            this.buy = buy;
        }

        public Map<String, String> getSell() {
            return sell;
        }

        public void setSell(Map<String, String> sell) {
            this.sell = sell;
        }
    }
}
