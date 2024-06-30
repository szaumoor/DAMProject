package com.rumpel.rumpelandroid.db;

import static com.rumpel.rumpelandroid.db.utils.Filters.and;
import static com.rumpel.rumpelandroid.db.utils.Filters.eq;
import static com.rumpel.rumpelandroid.db.utils.Filters.in;
import static com.szaumoor.rumple.model.interfaces.Entity.ID_FIELD;
import static com.szaumoor.rumple.model.interfaces.Entity.USER_ID_FIELD;

import android.content.Context;

import com.szaumoor.rumple.db.PluralDAO;
import com.szaumoor.rumple.model.interfaces.AbstractEntity;

import org.bson.BsonObjectId;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Skeletal base DAO implementation for plural DAOs (currently all except users).
 *
 * @param <T> Entity type
 */
public abstract class AbstractPluralDAO<T extends AbstractEntity<Object>> extends AbstractBaseDAO<T> implements PluralDAO<T> {
    /**
     * Protected constructor that delegates the MongoDB connection to the superclass.
     *
     * @param context  Context of the application
     * @param collName Name of the collection
     */
    protected AbstractPluralDAO(final Context context, final String collName) {
        super(context, collName);
    }
    /**
     * Retrieves a list of entities from the database based on the given mapping function.
     *
     * @param mappingFunction the function used to map the database document to an entity
     * @return a list of entities retrieved from the database
     */
    protected List<T> crudGetAll(final Function<Document, Optional<T>> mappingFunction) {
        final var executor = Executors.newSingleThreadExecutor();
        final var list = new ArrayList<T>(20);
        executor.submit(() -> {
            var it = collection.find(eq(USER_ID_FIELD, DAOUser.getLoggedUser().getId()));
            var cursor = it.iterator().get();
            cursor.forEachRemaining(document -> list.add(mappingFunction.apply(document).orElseThrow()));
            cursor.close();
        });
        executor.shutdown();
        try {
            executor.awaitTermination(120, TimeUnit.SECONDS);
            return list;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Retrieves a list of entities from the database based on the given mapping function and a list of object ids.
     *
     * @param mappingFunction the function used to map the database document to an entity
     * @return a list of entities retrieved from the database
     */
    protected List<T> crudGetAllById(final Function<Document, Optional<T>> mappingFunction, final List<Object> ids) {
        final var executor = Executors.newSingleThreadExecutor();
        var list = new ArrayList<T>(20);
        executor.submit(() -> {
            var it = collection.find(in(ID_FIELD, ids, BsonObjectId.class));
            var cursor = it.iterator().get();
            cursor.forEachRemaining(document -> list.add(mappingFunction.apply(document).orElseThrow()));
            cursor.close();
        });
        executor.shutdown();
        try {
            executor.awaitTermination(120, TimeUnit.SECONDS);
            return list;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Retrieves a list of filtered entities from the collection.
     *
     * @param mappingFunction the mapping function to apply to each document in the collection
     * @param extraFilter     an additional filter to apply to the collection query
     * @return a list of entities that match the provided filter
     */
    protected List<T> crudGetAllByFilter(final Function<Document, Optional<T>> mappingFunction, final Document extraFilter) {
        final var executor = Executors.newSingleThreadExecutor();
        var list = new ArrayList<T>(20);
        executor.submit(() -> {
            var it = collection.find(and(eq(USER_ID_FIELD, DAOUser.getLoggedUser().getId()), extraFilter));
            var cursor = it.iterator().get();
            cursor.forEachRemaining(document -> list.add(mappingFunction.apply(document).orElseThrow()));
            cursor.close();
        });
        executor.shutdown();
        try {
            executor.awaitTermination(120, TimeUnit.SECONDS);
            return list;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
