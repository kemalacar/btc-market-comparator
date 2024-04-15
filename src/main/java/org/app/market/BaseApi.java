package org.app.market;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.app.CoinRepository;
import org.app.JavaWebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Kemal Acar
 */
public abstract class BaseApi {
    protected ObjectMapper objectMapper = new ObjectMapper();
    protected CoinRepository coinRepository;
    protected JavaWebSocketClient client;

    public BaseApi(CoinRepository calculator) {
        this.coinRepository = calculator;
        JavaTimeModule module = new JavaTimeModule();
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME);
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        module.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME));
        objectMapper.registerModule(module);
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

    public void subscribe() {}

    String getSocketUrl() {return "default";}

    protected void onMessage(String msg) {}
}

