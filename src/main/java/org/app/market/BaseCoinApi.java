package org.app.market;

import org.app.repository.CoinRepository;
import org.app.socket.JavaWebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author anercan
 */
public abstract class BaseCoinApi extends BaseApi {

    protected JavaWebSocketClient client;

    public BaseCoinApi(CoinRepository calculator) {
        super(calculator);
    }

    public BaseCoinApi connect() {
        try {
            client = new JavaWebSocketClient(this::onMessage, new URI(getSocketUrl()));
            client.connectBlocking();
        } catch (URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public abstract void subscribe(String coinName);

    protected abstract String getSocketUrl();

    protected abstract void onMessage(String msg);
}
