package org.app;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Kemal Acar
 */
public class Database {
    final MongoDatabase database;

    public Database() {
        this.database = MongoClients.create("mongodb://localhost:27017").getDatabase("btc");
    }

    public void saveToDb(Calculator.Param param) {
        MongoCollection<Document> collection = database.getCollection("BTC_PRICES");
        Document document = new Document();

        document.put("market", param.market);
        document.put("coin", param.coin);
        document.put("value", param.price);
        document.put("dateTime", param.dateTime);
        collection.insertOne(document);
    }

    public void saveToDb(Calculator.LastValue lastValue) {
        MongoCollection<Document> collection = database.getCollection("BTC_MARKETS_PRICE");
        Document document = new Document();
        if (Objects.equals(lastValue.binancePrice, BigDecimal.ZERO) || lastValue.paribuPrice == null || lastValue.btcTurkPrice == null) {
            return;
        }

        document.put("binance", lastValue.binancePrice);
        document.put("btc_turk", lastValue.btcTurkPrice);
        document.put("paribu", lastValue.paribuPrice);
        document.put("dateTime", LocalDateTime.now());
        collection.insertOne(document);
    }

    public List<Document> getAll() {
        MongoCollection<Document> collection = database.getCollection("BTC_MARKETS_PRICE");
        List<Document> docs = new ArrayList<>();
        collection.find().forEach(docs::add);
        return docs;
    }
}

