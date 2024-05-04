package org.app.config.socket;


@FunctionalInterface
public interface MessageCallBack {
    void onMessage(String response) ;
}
