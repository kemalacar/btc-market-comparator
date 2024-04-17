package org.app.market;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.app.CoinRepository;
import org.app.JavaWebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Kemal Acar
 */
public abstract class BaseApi {
    protected static ObjectMapper objectMapper = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
            .enable(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION);

    protected CoinRepository coinRepository;
    protected JavaWebSocketClient client;

    public BaseApi(CoinRepository calculator) {
        this.coinRepository = calculator;
    }

    public BaseApi connect() {
        try {
            client = new JavaWebSocketClient(this::onMessage, new URI(getSocketUrl()));
            client.connectBlocking();
        } catch (URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public void subscribe(String coinName) {
    }

    String getSocketUrl() {
        return "default";
    }

    protected void onMessage(String msg) {
    }
}

