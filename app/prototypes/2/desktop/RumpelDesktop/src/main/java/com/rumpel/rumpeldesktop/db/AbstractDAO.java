package com.rumpel.rumpeldesktop.db;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.db.utils.Procedure;
import com.szaumoor.rumple.model.entities.User;
import com.szaumoor.rumple.model.interfaces.AbstractEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.szaumoor.rumple.model.interfaces.Entity.ID_FIELD;
import static com.szaumoor.rumple.model.interfaces.Entity.USER_ID_FIELD;

public abstract class AbstractDAO {

    private static MongoClient client;

    protected static MongoDatabase database;
    protected final MongoCollection<Document> collection;

    protected static final Logger logger = LogManager.getLogger();

    protected AbstractDAO(final String collName) {
        if (Objects.isNull(client)) {
            logger.info("Attempting to connect to MongoDB");
            var connectionString = "mongodb+srv://non_admin:srPZvhS7KcZ2BEug@cluster0.t1nl1se.mongodb.net/?retryWrites=true&w=majority"; // needs to ultimately adapt to a safe connection string
            var serverApi = ServerApi.builder()
                    .version(ServerApiVersion.V1)
                    .build();
            var settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(connectionString))
                    .serverApi(serverApi)
                    .build();

            client = MongoClients.create(settings);
            database = client.getDatabase("rumpel");
            collection = database.getCollection(collName);
            try {
                database.runCommand(new Document("ping", 1));
                logger.info("Pinged your deployment. You successfully connected to MongoDB!");
            } catch (final MongoException ex) {
                logger.error("Failed to connect to Mongo");
            }
        } else {
            logger.info("Already connected to MongoDB, no new connection was made");
            collection = database.getCollection(collName);
        }
    }

    public static void closeClient() {
        if (client != null) client.close();
        client = null;
        logger.info("MongoDB client closed");
    }

    protected final <T extends AbstractEntity<Object>> Optional<T> crudGet(final Bson filter, final User user, final Function<Document, Optional<T>> mappingFunction) {
        return mappingFunction.apply(collection.find(and(eq(USER_ID_FIELD, user.getId()), filter)).first());
    }

    protected final <T extends AbstractEntity<Object>> List<T> crudGetAll(final User user, final Function<Document, Optional<T>> mappingFunction) {
        return collection.find(Filters.eq(USER_ID_FIELD, user.getId()))
                .map(mappingFunction::apply)
                .map(Optional::orElseThrow)
                .into(new ArrayList<>());
    }

    protected final <T extends AbstractEntity<Object>> Outcome crudInsert(final T object, final Supplier<Boolean> checker, final Function<T, Optional<Document>> mappingFunction) {
        if (Objects.isNull(object)) return Outcome.NULL;
        if (checker.get()) return Outcome.UNIQUE_EXISTS;
        var opDoc = mappingFunction.apply(object);
        if (opDoc.isEmpty()) return Outcome.ERROR;
        var insertOneResult = collection.insertOne(opDoc.get());
        if (insertOneResult.wasAcknowledged()) object.setId(insertOneResult.getInsertedId());
        else return Outcome.ERROR;
        return Outcome.SUCCESS;
    }

    protected final <T extends AbstractEntity<Object>> Outcome crudModify(final T object, final User user, final Function<T, Optional<Document>> mappingFunction) {
        var document = mappingFunction.apply(object);
        var filter = eq(ID_FIELD, (BsonObjectId) object.getId());
        if (user != null) filter = and(filter, eq(USER_ID_FIELD, (BsonObjectId) user.getId()));
        if (document.isEmpty()) return Outcome.ERROR;
        var update = collection.updateOne(filter, new Document("$set", document.get()));
        if (update.getMatchedCount() == 0) return Outcome.ERROR;
        return Outcome.SUCCESS;
    }

    protected final <T extends AbstractEntity<Object>> Outcome crudDelete(final T object, final Function<T, Optional<Document>> mappingFunction, final Procedure cascadeDelete) {
        var filter = mappingFunction.apply(object);
        if (filter.isEmpty()) return Outcome.ERROR;
        var session = client.startSession();
        try {
            session.startTransaction();
            var deleteResult = collection.deleteOne(filter.get());
            if (deleteResult.getDeletedCount() != 1) {
                session.abortTransaction();
                return Outcome.ERROR;
            } else {
                // code for cascade edits
                session.commitTransaction();
                return Outcome.SUCCESS;
            }
        } catch (final MongoException exception) {
            session.abortTransaction();
            return Outcome.ERROR;
        } finally {
            session.close();
        }
    }

    protected final <T extends AbstractEntity<Object>> Outcome crudDeleteAll(final User user, final Function<Document, Optional<T>> mappingFunction) {
        var session = client.startSession();
        try {
            collection.deleteMany(eq(USER_ID_FIELD, DAOUser.getLoggedUser()));
            var docsNow = crudGetAll(user, mappingFunction).size();
            if (docsNow > 0) return Outcome.ERROR;
            return Outcome.SUCCESS;
        } catch (final MongoException ex) {
            session.abortTransaction();
            return Outcome.ERROR;
        } finally {
            session.close();
        }
    }

}
