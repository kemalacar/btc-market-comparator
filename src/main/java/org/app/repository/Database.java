package org.app.repository;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.app.market.data.ExchangeCoin;
import org.bson.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kemal Acar
 */
public class Database {
    final MongoDatabase database;

    public Database() {
        this.database = MongoClients.create("mongodb://localhost:27017").getDatabase("coinRecords");
    }

    public void saveDbToCoinList(List<ExchangeCoin> paramList, String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        List<Document> documentList = new ArrayList<>();
        for (ExchangeCoin param : paramList) {
            long marketEventTime = Long.parseLong(param.marketEventTime);
            Document document = new Document();
            document.put("ask", param.ask);
            document.put("bid", param.bid);
            document.put("dateTime", LocalDateTime.ofInstant(Instant.ofEpochMilli(param.dateTime), ZoneOffset.UTC));
            document.put("marketDifferencePercentage", param.marketDifferencePercentage);
            document.put("marketEventTime", LocalDateTime.ofInstant(Instant.ofEpochMilli(marketEventTime), ZoneOffset.UTC));
            document.put("marketPrice", param.marketPrice);
            document.put("timeDifference", (param.dateTime - marketEventTime));

            documentList.add(document);
        }
        collection.insertMany(documentList);
    }
}

