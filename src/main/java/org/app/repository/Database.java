package org.app.repository;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * @author Kemal Acar
 */
public class Database {
    final MongoDatabase database;

    public Database() {
        this.database = MongoClients.create("mongodb://localhost:27017").getDatabase("coinRecords");
    }

    public void saveDbToCoin(CoinRepository.Param param, String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document document = new Document();
        document.put("ask", param.ask);
        document.put("bid",param.bid);
        document.put("dateTime", param.dateTime);

        collection.insertOne(document);
    }

    public void saveMarketPrice(CoinRepository.MainMarketCoin mainMarketCoin, String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document document = new Document();
        document.put("price", mainMarketCoin.price);
        document.put("quantity",mainMarketCoin.quantity);
        document.put("eventTimeInMarket", mainMarketCoin.eventTime);
        document.put("saveTime", mainMarketCoin.saveTime);

        collection.insertOne(document);
    }


}

