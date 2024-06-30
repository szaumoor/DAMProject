package com.rumpel.rumpelandroid.realm;

import static com.rumpel.rumpelandroid.realm.Filters.eq;

import android.content.Context;

import com.szaumoor.rumple.model.entities.Tag;
import com.szaumoor.rumple.model.interfaces.AbstractEntity;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

import io.realm.Realm;
import io.realm.mongodb.mongo.MongoCollection;

public abstract class AbstractPluralDAO<T extends AbstractEntity<Object>> extends AbstractDAO implements PluralDAO<T> {
    protected AbstractPluralDAO(Context context, String collName) {
        super(context, collName);
    }

    protected static <T> List<T> crudGetAll(MongoCollection<Document> coll, String userId, Function<Document, T> procedure){
        final var executor = Executors.newSingleThreadExecutor();
        final var list = new ArrayList<T>(20);
        executor.submit(() -> {
            final Realm realm = Realm.getDefaultInstance();
            var it = coll.find(eq(userId, DAOUser.getLoggedUser().getId()));
            it.iterator().get().forEachRemaining(document -> list.add(procedure.apply(document)));

            realm.close();
        });
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
            return list;
        } catch (InterruptedException e) {
            Logs.interrupt(AbstractPluralDAO.class.getSimpleName());
            throw new RuntimeException(e);
        }
    }
}
