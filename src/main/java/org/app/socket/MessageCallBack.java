package org.app.socket;


@FunctionalInterface
public interface MessageCallBack {
    void onMessage(String response) ;
}
