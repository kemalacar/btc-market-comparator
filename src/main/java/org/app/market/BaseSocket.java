package org.app.market;

import org.app.config.socket.JavaWebSocketClient;
import org.app.repository.CoinRepository;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author anercan
 */
public abstract class BaseSocket extends BaseApi {

    protected JavaWebSocketClient client;
    protected CoinRepository coinRepository;

    public BaseSocket(CoinRepository coinRepository) {
        this.coinRepository = coinRepository;
    }

    public BaseSocket connect() {
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
