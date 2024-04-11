package org.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import org.app.market.BinanceApi;
import org.app.market.BtcTurkApi;
import org.app.market.ParibuApi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Kemal Acar
 */
public class Main {

    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final int STATUS_OK = 200;


    public static void main(String[] args) throws IOException {

        Calculator calculator = new Calculator();

        new ParibuApi(calculator).connect().subscribe();
        new BtcTurkApi(calculator).connect().subscribe();
        new BinanceApi(calculator).subscribe();


        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);

        server.createContext("/test", he -> {
            final Headers headers = he.getResponseHeaders();

            headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
            final byte[] rawResponseBody = new ObjectMapper().writeValueAsString(calculator.getLastValues()).getBytes(CHARSET);
            he.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
            he.getResponseBody().write(rawResponseBody);
            he.close();
        });
        server.start();
        System.out.println(" Server started on port 8001");

    }

}

