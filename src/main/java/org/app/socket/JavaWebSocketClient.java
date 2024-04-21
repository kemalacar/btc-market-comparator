package org.app.socket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class JavaWebSocketClient extends WebSocketClient {
    MessageCallBack messageCallBack;

    public JavaWebSocketClient(MessageCallBack messageCallBack, URI serverUri) {
        super(serverUri);
        this.messageCallBack = messageCallBack;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.printf("Connected -> %s %s", serverHandshake.getHttpStatus(), serverHandshake.getHttpStatusMessage());
    }

    @Override
    public void onMessage(String message) {
        messageCallBack.onMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
