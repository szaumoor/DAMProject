package com.rumpel.rumpelandroid.db;

import static com.rumpel.rumpelandroid.db.utils.Filters.and;
import static com.rumpel.rumpelandroid.db.utils.Filters.eq;

import android.content.Context;

import com.rumpel.rumpelandroid.db.utils.Documents;
import com.rumpel.rumpelandroid.utils.Logs;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.Tag;
import com.szaumoor.rumple.model.interfaces.Entity;

import org.bson.BsonObjectId;
import org.bson.Document;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.realm.mongodb.mongo.result.InsertOneResult;
import io.realm.mongodb.mongo.result.UpdateResult;

public class DAOTags extends AbstractPluralDAO<Tag> {

    public DAOTags(final Context context) {
        super(context, Tag.COLL_TAGS);
    }

    @Override
    public Optional<Tag> get(final Object id) {
        return crudGet(eq(Tag.NAME_FIELD, id.toString()), Documents::toTag, false);
    }

    @Override
    public Optional<Tag> getById(final Object id) {
        return crudGet(eq(Entity.ID_FIELD, id), Documents::toTag, false);
    }

    @Override
    public List<Tag> getAll() {
        return crudGetAll(Documents::toTag);
    }

    @Override
    public List<Tag> getAllById(final List<Object> list) {
        return null;
    }

    @Override
    public Outcome insertAll(final List<Tag> elements) {
        return null;
    }

    @Override
    public Outcome deleteAll(final List<Tag> list) {
        return null;
    }

    @Override
    public Outcome deleteAll() {
        return null;
    }

    @Override
    public Outcome insert(final Tag tag) {
        if (Objects.isNull(tag)) throw new NullPointerException();
        final Document doc = Documents.tagToDocument(tag).get();
        final var executor = Executors.newSingleThreadExecutor();
        Optional<Tag> pm = get(tag.getName());
        if (pm.isPresent()) return Outcome.UNIQUE_EXISTS;
        executor.submit(() -> {
            InsertOneResult insertOneResult = coll.insertOne(doc).get();
            tag.setId(insertOneResult.getInsertedId().asObjectId());
        });
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
    public Outcome delete(final Tag tag) {
        if (Objects.isNull(tag)) throw new NullPointerException();
        final Document doc = Documents.tagToDocument(tag).get();
        final var executor = Executors.newSingleThreadExecutor();
        Optional<Tag> pm = get(tag.getName());
        if (pm.isEmpty()) return Outcome.NOT_FOUND;
        executor.submit(() -> coll.deleteOne(doc).get());
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
    public Outcome modify(final Tag tag) {
        if (Objects.isNull(tag)) return Outcome.NULL;
        final var executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            UpdateResult updateResult = coll.updateOne(
                    and(
                            eq(Entity.ID_FIELD, (BsonObjectId) tag.getId()),
                            eq(Entity.USER_ID_FIELD, DAOUser.getLoggedUser().getId())
                    ),
                    new Document("$set", Documents.tagToDocument(tag).orElseThrow())).get();
        });
        executor.shutdown();
        try {
            boolean b = executor.awaitTermination(10, TimeUnit.SECONDS);
            return b ? Outcome.SUCCESS : Outcome.TIMEOUT;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
