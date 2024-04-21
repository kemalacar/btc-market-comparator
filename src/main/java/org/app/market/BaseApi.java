package org.app.market;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.app.repository.CoinRepository;

/**
 * @author Kemal Acar
 */
public abstract class BaseApi {
    protected static ObjectMapper objectMapper = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);

    protected CoinRepository coinRepository;

    public BaseApi(CoinRepository calculator) {
        this.coinRepository = calculator;
    }
}

