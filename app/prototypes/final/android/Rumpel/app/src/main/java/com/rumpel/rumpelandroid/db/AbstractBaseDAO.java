package com.rumpel.rumpelandroid.db;

import static com.rumpel.rumpelandroid.db.utils.Filters.and;
import static com.rumpel.rumpelandroid.db.utils.Filters.eq;
import static com.szaumoor.rumple.model.interfaces.Entity.ID_FIELD;
import static com.szaumoor.rumple.model.interfaces.Entity.USER_ID_FIELD;

import android.content.Context;

import com.szaumoor.rumple.db.BaseDAO;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.User;
import com.szaumoor.rumple.model.interfaces.AbstractEntity;

import org.bson.Document;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

import io.realm.mongodb.mongo.result.DeleteResult;

/**
 * Skeletal base DAO implementation for non-plural DAOs (currently only users).
 *
 * @param <T> Entity type
 */
public abstract class AbstractBaseDAO<T extends AbstractEntity<Object>> extends AbstractDAO implements BaseDAO<T> {
    /**
     * Protected constructor that delegates the MongoDB connection to the superclass.
     *
     * @param collName Name of the collection
     */
    protected AbstractBaseDAO(final Context context, final String collName) {
        super(context, collName);
    }

    /**
     * This function performs a CRUD get operation by querying the collection with the provided filter and the user ID.
     *
     * @param primaryFilter the filter to be used in the query
     * @param function      the mapping function to transform the retrieved document into an optional entity
     * @param users         boolean flag to indicate whether we are querying for a user or not
     * @return an optional entity of type T, representing the result of the get operation
     */
    protected Optional<T> crudGet(final Document primaryFilter, final Function<Document, Optional<T>> function, final boolean users) {
        final var executor = Executors.newSingleThreadExecutor();
        final AtomicReference<T> ref = new AtomicReference<>();
        var filter = new AtomicReference<>(primaryFilter);
        if (!users)
            filter.set(and(filter.get(), eq(USER_ID_FIELD, DAOUser.getLoggedUser().getId())));
        executor.submit(() -> {
            final Document doc = collection.findOne(filter.get()).get();
            ref.set(function.apply(doc).orElseThrow());
        });
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
            return Optional.ofNullable(ref.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inserts an object into the database.
     *
     * @param object          the object to be inserted
     * @param checker         a supplier providing a confirmation of whether the object already exists
     * @param mappingFunction a function to map the object to a document
     * @return the outcome of the insertion operation
     */
    protected final Outcome crudInsert(final T object, final Supplier<Boolean> checker, final Function<T, Optional<Document>> mappingFunction) {
        if (Objects.isNull(object)) return Outcome.NULL;
        if (checker.get()) return Outcome.UNIQUE_EXISTS;
        var opDoc = mappingFunction.apply(object);
        if (opDoc.isEmpty()) return Outcome.ERROR;
        final var executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            var insertOneResult = collection.insertOne(opDoc.get()).get();
            object.setId(insertOneResult.getInsertedId().asObjectId());
        });
        executor.shutdown();
        try {
            boolean b = executor.awaitTermination(10, TimeUnit.SECONDS);
            if (b) return Outcome.SUCCESS;
            else return Outcome.TIMEOUT;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Deletes an object from the database.
     *
     * @param object          the object to be deleted
     * @param mappingFunction the function that maps the object to a filter
     * @return the outcome of the delete operation (SUCCESS or ERROR)
     */
    protected final Outcome crudDelete(final T object, final Function<T, Optional<Document>> mappingFunction) {
        if (Objects.isNull(object)) throw new NullPointerException();
        final var filter = mappingFunction.apply(object);
        if (filter.isEmpty()) throw new RuntimeException("Filter cannot be empty");
        final var executor = Executors.newSingleThreadExecutor();
        final long[] deletedRecords = {0};
        executor.submit(() -> {
            DeleteResult deleteResult = collection.deleteOne(filter.get()).get();
            deletedRecords[0] = deleteResult.getDeletedCount();
        });
        executor.shutdown();
        try {
            boolean b = executor.awaitTermination(10, TimeUnit.SECONDS);
            if (b) {
                if (deletedRecords[0] > 0) return Outcome.SUCCESS;
                else return Outcome.ERROR;
            } else return Outcome.TIMEOUT;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Modifies an object in the database.
     *
     * @param object          the object to modify
     * @param mappingFunction the function used to map the object to a document
     * @return the outcome of the modification operation
     */
    protected final Outcome crudModify(final T object, final Function<T, Optional<Document>> mappingFunction) {
        if (Objects.isNull(object)) return Outcome.NULL;
        final var executor = Executors.newSingleThreadExecutor();
        var filter = new AtomicReference<>(eq(ID_FIELD, object.getId()));
        if (!(object instanceof User))
            filter.set(and(filter.get(), eq(USER_ID_FIELD, DAOUser.getLoggedUser().getId())));
        var modified = new AtomicLong();
        executor.execute(() -> {
            var updateResult = collection.updateOne(filter.get(), new Document("$set", mappingFunction.apply(object).orElseThrow())).get();
            modified.set(updateResult.getModifiedCount());
        });
        executor.shutdown();
        try {
            boolean b = executor.awaitTermination(10, TimeUnit.SECONDS);
            if (b) {
                if (modified.get() == 0) return Outcome.ERROR;
                else return Outcome.SUCCESS;
            } else return Outcome.TIMEOUT;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
