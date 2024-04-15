package org.app;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;

/**
 * @author Kemal Acar
 */
public class Database {
    final MongoDatabase database;

    public Database() {
        this.database = MongoClients.create("mongodb://localhost:27017").getDatabase("coinRecords");
    }

    public void saveToDb(CoinRepository.Param param, String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document document = new Document();
        document.put("value", param.price);
        document.put("amount",param.amount);
        document.put("dateTime", param.dateTime);
        InsertOneResult insertOneResult = collection.insertOne(document);

        System.out.printf("\n Data Inserted: %s", insertOneResult);
    }
}

