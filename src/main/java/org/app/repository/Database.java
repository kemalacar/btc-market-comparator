package org.app.repository;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

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

    public void saveDbToCoinList(List<CoinRepository.Param> paramList, String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        List<Document> documentList = new ArrayList<>();
        for (CoinRepository.Param param:paramList) {
            Document document = new Document();
            document.put("ask", param.ask);
            document.put("bid",param.bid);
            document.put("dateTime", param.dateTime);
            documentList.add(document);
        }
        collection.insertMany(documentList);
    }

    public void saveMarketPrice(List<CoinRepository.MainMarketCoin> paramList, String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        List<Document> documentList = new ArrayList<>();
        for (CoinRepository.MainMarketCoin param:paramList) {
            Document document = new Document();
            document.put("price", param.price);
            document.put("quantity",param.quantity);
            document.put("eventTimeInMarket", param.eventTime);
            document.put("saveTime", param.saveTime);
            documentList.add(document);
        }
        collection.insertMany(documentList);
    }


}

