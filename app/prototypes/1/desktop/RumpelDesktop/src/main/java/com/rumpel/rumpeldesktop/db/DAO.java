package com.rumpel.rumpeldesktop.db;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.rumpel.rumpeldesktop.db.utils.Outcome;

import com.szaumoor.rumple.model.interfaces.Entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

abstract class DAO<I, T extends Entity> {

    private static MongoClient client;
    private static boolean connected;

    protected static MongoDatabase database;
    protected MongoCollection<Document> collection;

    protected static final Logger logger = LogManager.getLogger();

    protected DAO() {
        if (Objects.isNull(client)) {
            logger.info("Attempting to connect to MongoDB");
            String connectionString = "mongodb+srv://non_admin:srPZvhS7KcZ2BEug@cluster0.t1nl1se.mongodb.net/?retryWrites=true&w=majority";

            ServerApi serverApi = ServerApi.builder()
                    .version(ServerApiVersion.V1)
                    .build();
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(connectionString))
                    .serverApi(serverApi)
                    .build();

            client = MongoClients.create(settings);
            database = client.getDatabase("rumpel");
            try {
                database.runCommand(new Document("ping", 1));
                logger.info("Pinged your deployment. You successfully connected to MongoDB!");
                connected = true;
            } catch (MongoException ex) {
                connected = false;
                logger.error("Failed to connect to Mongo");
            }
        } else logger.info("Already connected to MongoDB, no new connection was made");
    }

    protected abstract Optional<T> get(final I id);

    protected Optional<T> getById(final ObjectId id) {
        return Optional.empty();
    }

    protected abstract List<T> getAll();

    protected abstract Outcome insert(final T object);

    public static void closeClient() {
        client.close();
        client = null;
        connected = false;
        logger.info("MongoDB client closed");
    }

    public static boolean isConnected() {
        return connected;
    }

    public static MongoDatabase getDatabase() {
        return database;
    }
}
