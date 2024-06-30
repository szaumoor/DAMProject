package com.rumpel.rumpeldesktop.db;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.szaumoor.rumple.db.utils.Outcome;
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

/**
 * Class that handles the connection to the database. Provides generic methods for the CRUD operations for subclasses.
 */
public abstract class AbstractDAO {
    private static MongoClient client;
    protected static MongoDatabase database;
    protected final MongoCollection<Document> collection;
    protected static final Logger logger = LogManager.getLogger();

    /**
     * Base constructor all DAOs, attempts to connect to MongoDB if connection wasn't established previously.
     *
     * @param collName Collection name for retrieval
     */
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
        } else collection = database.getCollection(collName);
    }

    /**
     * Close the MongoDB client.
     */
    public static void closeClient() {
        if (client != null) client.close();
        client = null;
        logger.info("MongoDB client closed");
    }

    /**
     * This function performs a CRUD get operation by querying the collection with the provided filter and the user ID.
     *
     * @param filter          the filter to be used in the query
     * @param user            the user object representing the current user
     * @param mappingFunction the mapping function to transform the retrieved document into an optional entity
     * @param <T>             the type of the entity
     * @return an optional entity of type T, representing the result of the get operation
     */
    protected final <T extends AbstractEntity<Object>> Optional<T> crudGet(final Bson filter, final User user, final Function<Document, Optional<T>> mappingFunction) {
        return mappingFunction.apply(collection.find(and(eq(USER_ID_FIELD, user.getId()), filter)).first());
    }

    /**
     * Retrieves a list of entities from the database based on the given user and mapping function.
     *
     * @param user            the user for which to retrieve the entities
     * @param mappingFunction the function used to map the database document to an entity
     * @return a list of entities retrieved from the database
     */
    protected final <T extends AbstractEntity<Object>> List<T> crudGetAll(final User user, final Function<Document, Optional<T>> mappingFunction) {
        return collection.find(eq(USER_ID_FIELD, user.getId()))
                .map(mappingFunction::apply)
                .map(Optional::orElseThrow)
                .into(new ArrayList<>());
    }

    /**
     * Retrieves a list of filtered entities from the collection.
     *
     * @param user            the user object representing the current user
     * @param mappingFunction the mapping function to apply to each document in the collection
     * @param extraFilter     an additional filter to apply to the collection query
     * @return a list of entities that match the provided filter
     */
    protected final <T extends AbstractEntity<Object>> List<T> crudGetAllFiltered(final User user, final Function<Document, Optional<T>> mappingFunction, final Bson extraFilter) {
        return collection.find(and(eq(USER_ID_FIELD, user.getId()), extraFilter))
                .map(mappingFunction::apply)
                .map(Optional::orElseThrow)
                .into(new ArrayList<>());
    }

    /**
     * Inserts an object into the database.
     *
     * @param object          the object to be inserted
     * @param checker         a supplier providing a confirmation of whether the object already exists
     * @param mappingFunction a function to map the object to a document
     * @return the outcome of the insertion operation
     */
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

    /**
     * Modifies an object in the database.
     *
     * @param object          the object to modify
     * @param user            the user performing the modification
     * @param mappingFunction the function used to map the object to a document
     * @return the outcome of the modification operation
     */
    protected final <T extends AbstractEntity<Object>> Outcome crudModify(final T object, final User user, final Function<T, Optional<Document>> mappingFunction) {
        var document = mappingFunction.apply(object);
        var filter = eq(ID_FIELD, (BsonObjectId) object.getId());
        if (user != null) filter = and(filter, eq(USER_ID_FIELD, (BsonObjectId) user.getId()));
        if (document.isEmpty()) return Outcome.ERROR;
        var update = collection.updateOne(filter, new Document("$set", document.get()));
        if (update.getMatchedCount() == 0) return Outcome.ERROR;
        return Outcome.SUCCESS;
    }

    /**
     * Deletes an object from the database.
     *
     * @param object          the object to be deleted
     * @param mappingFunction the function that maps the object to a filter
     * @return the outcome of the delete operation (SUCCESS or ERROR)
     */
    protected final <T extends AbstractEntity<Object>> Outcome crudDelete(final T object, final Function<T, Optional<Document>> mappingFunction) {
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

    /**
     * Deletes all entities associated with a specific user.
     *
     * @param user            the user for whom to delete entities
     * @param mappingFunction a function to map a document to an entity
     * @return the outcome of the delete operation
     */
    protected final <T extends AbstractEntity<Object>> Outcome crudDeleteAll(final User user, final Function<Document, Optional<T>> mappingFunction) {
        var session = client.startSession();
        try {
            collection.deleteMany(eq(USER_ID_FIELD, DAOUser.getLoggedUser().getId()));
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
