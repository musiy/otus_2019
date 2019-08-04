package com.musiy.dao;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;

public class MongoDaoFactory {

    private static final String DATABASE_NAME = "homework_23";

    private final MongoClient mongoClient;
    private final MongoDatabase database;

    private static MongoDaoFactory INSTANCE;

    private MongoDaoFactory() {
        mongoClient = MongoClients.create("mongodb://localhost");
        database = mongoClient.getDatabase(DATABASE_NAME);
    }

    public static MongoDaoFactory getInstance() {
        if (INSTANCE == null) {
            synchronized (MongoDaoFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MongoDaoFactory();
                }
            }
        }
        return INSTANCE;
    }

    public UsersDao getUsersDao() {
        return new UsersDaoMongo(database.getCollection("users"));
    }
}
