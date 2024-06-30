package com.rumpel.rumpelandroid.db;

import static com.rumpel.rumpelandroid.db.utils.Filters.eq;
import static com.szaumoor.rumple.model.interfaces.Entity.USER_ID_FIELD;

import android.content.Context;

import com.rumpel.rumpelandroid.utils.Logs;
import com.szaumoor.rumple.db.PluralDAO;
import com.szaumoor.rumple.model.interfaces.AbstractEntity;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import io.realm.Realm;

public abstract class AbstractPluralDAO<T extends AbstractEntity<Object>> extends AbstractBaseDAO<T> implements PluralDAO<T> {
    protected AbstractPluralDAO(Context context, String collName) {
        super(context, collName);
    }

    protected List<T> crudGetAll(final Function<Document, T> procedure){
        final var executor = Executors.newSingleThreadExecutor();
        final var list = new ArrayList<T>(20);
        executor.submit(() -> {
            var it = coll.find(eq(USER_ID_FIELD, DAOUser.getLoggedUser().getId()));
            it.iterator().get().forEachRemaining(document -> list.add(procedure.apply(document)));
        });
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
            System.out.println(list);
            return list;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
