package org.example.App;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.opencsv.CSVWriter;
import org.bson.Document;
import org.example.Utils.DBConnector;

import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DPApp {

    private DBConnector mongoDBConfig = new DBConnector();


    public void buildConnection() {
        mongoDBConfig.createMongoConnection("localhost:27017", "dpDataset");
    }

    public void runApp() throws Exception {

        MongoDatabase db = mongoDBConfig.getDatabase();

        for (String collectionName : db.listCollectionNames()) {
            MongoCollection<Document> collection = db.getCollection(collectionName);

            try (CSVWriter writer = new CSVWriter(new FileWriter(collectionName + ".csv"))) {
                MongoCursor<Document> cursor = collection.find().iterator();
                while (cursor.hasNext()) {
                    Document document = cursor.next();
                    String[] line = document.toJson().split(",", -1);
                    writer.writeNext(line);
                }
            }
        }

        mongoDBConfig.getClient().close();


    }


}
