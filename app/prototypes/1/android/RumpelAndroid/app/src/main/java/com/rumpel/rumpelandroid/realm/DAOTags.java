package com.rumpel.rumpelandroid.realm;

import static com.rumpel.rumpelandroid.realm.Filters.and;
import static com.rumpel.rumpelandroid.realm.Filters.eq;

import android.content.Context;

import com.szaumoor.rumple.model.entities.PaymentMethod;
import com.szaumoor.rumple.model.entities.Tag;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;

public class DAOTags extends AbstractPluralDAO<Tag> {

    public static final String COLL_TAGS = "tags";
    public static final String NAME_FIELD = "name";
    public static final String USER_ID_FIELD = "user_id";

    private static final String CLASS_TAG = DAOTags.class.getSimpleName();

    public DAOTags(final Context context) {
        super(context, COLL_TAGS);
    }

    @Override
    public Optional<Tag> get(final Object id) {
        final var executor = Executors.newSingleThreadExecutor();
        final AtomicReference<Tag> ref = new AtomicReference<>();
        executor.submit(() -> {
            final Realm realm = Realm.getDefaultInstance();
            final Document doc = coll.findOne(and(
                    eq(NAME_FIELD, id.toString()),
                    eq(USER_ID_FIELD, DAOUser.getLoggedUser().getId()))).get();
            ref.set(Documents.toTag(doc));
            realm.close();
        });
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
            return Optional.ofNullable(ref.get());
        } catch (InterruptedException e) {
            Logs.interrupt(CLASS_TAG);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Tag> getAll() {
        return AbstractPluralDAO.crudGetAll(coll, USER_ID_FIELD, Documents::toTag);
    }

    @Override
    public Outcome insertAll(final List<Tag> elements) {
        return null;
    }

    @Override
    public Outcome insert(final Tag tag) {
        if (Objects.isNull(tag)) throw new NullPointerException();
        final Document doc = Documents.tagToDocument(tag).get();
        final var executor = Executors.newSingleThreadExecutor();
        Optional<Tag> pm = get(tag.getName());
        if (pm.isPresent()) return Outcome.UNIQUE_EXISTS;
        executor.submit(() -> coll.insertOne(doc).get());
        executor.shutdown();
        try {
            boolean b = executor.awaitTermination(10, TimeUnit.SECONDS);
            if (b) return Outcome.SUCCESS;
            else return Outcome.TIMEOUT;
        } catch (InterruptedException e) {
            Logs.interrupt(getClass().getSimpleName());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Outcome modify(final Tag entity) {
        return null;
    }
}
