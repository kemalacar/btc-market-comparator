package org.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.app.market.BinanceApi;
import org.app.market.BtcTurkApi;
import org.app.market.ParibuApi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Kemal Acar
 */
public class Main {

    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final int STATUS_OK = 200;


    public static void main(String[] args) throws IOException {

        boolean runSockets = true;
        boolean runTimer = true;


        Database database = new Database();
        Calculator calculator = new Calculator(database);

        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);

        server.createContext("/test", he -> {
            final Headers headers = he.getResponseHeaders();
            if (disableCors(he)) return;
            headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
            final byte[] rawResponseBody = new ObjectMapper().writeValueAsString(calculator.getLastValue()).getBytes(CHARSET);
            he.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
            he.getResponseBody().write(rawResponseBody);
            he.close();
        });

        server.createContext("/all", he -> {

            if (disableCors(he)) return;

            final Headers headers = he.getResponseHeaders();
            headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
            final byte[] rawResponseBody = new ObjectMapper().writeValueAsString(database.getAll()).getBytes(CHARSET);
            he.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
            he.getResponseBody().write(rawResponseBody);
            he.close();
        });


        server.start();
        System.out.println(" Server started on port 8001");

        if (runSockets) {
            new ParibuApi(calculator).connect().subscribe();
            new BtcTurkApi(calculator).connect().subscribe();
            new BinanceApi(calculator).subscribe();
        }

        if (runTimer) {
            ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                database.saveToDb(calculator.getLastValue());
            }, 0, 1, TimeUnit.SECONDS);
        }

    }

    private static boolean disableCors(HttpExchange he) throws IOException {
        he.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        if (he.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            he.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
            he.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
            he.sendResponseHeaders(204, -1);
            return true;
        }
        return false;
    }

}

