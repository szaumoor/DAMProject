package com.rumpel.rumpelandroid.db;

import static com.rumpel.rumpelandroid.db.utils.Filters.and;
import static com.rumpel.rumpelandroid.db.utils.Filters.eq;

import android.content.Context;

import com.szaumoor.rumple.db.BaseDAO;
import com.szaumoor.rumple.model.interfaces.AbstractEntity;
import com.szaumoor.rumple.model.interfaces.Entity;

import org.bson.Document;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public abstract class AbstractBaseDAO<T extends AbstractEntity<Object>> extends AbstractDAO implements BaseDAO<T> {
    protected AbstractBaseDAO(final Context context, final String collName) {
        super(context, collName);
    }

    protected Optional<T> crudGet(final Document primaryFilter, final Function<Document, T> function, final boolean users) {
        final var executor = Executors.newSingleThreadExecutor();
        final AtomicReference<T> ref = new AtomicReference<>();
        var filter = new AtomicReference<>(primaryFilter);
        if (!users) filter.set(and(filter.get(), eq(Entity.USER_ID_FIELD, DAOUser.getLoggedUser().getId())));

        executor.submit(() -> {
            final Document doc = coll.findOne(filter.get()).get();
            ref.set(function.apply(doc));
        });
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
            return Optional.ofNullable(ref.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
