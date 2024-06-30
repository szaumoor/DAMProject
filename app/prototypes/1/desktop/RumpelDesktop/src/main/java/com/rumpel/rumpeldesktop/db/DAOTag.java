package com.rumpel.rumpeldesktop.db;

import com.mongodb.client.model.Filters;
import com.rumpel.rumpeldesktop.db.utils.Documents;
import com.rumpel.rumpeldesktop.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.Tag;
import com.szaumoor.rumple.model.entities.User;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public final class DAOTag extends DAO<String, Tag>{


    public static final String COLL_TAGS = "tags";
    public static final String NAME_FIELD = "name";
    public static final String USER_ID_FIELD = "user_id";

    private final ObjectId userId;

    public DAOTag(final User user) {
        collection = database.getCollection(COLL_TAGS);
        userId = (ObjectId) user.getId();
    }

    @Override
    public Optional<Tag> get(final String id) {
        return Documents.parseTag(collection.find(and(
                        eq(USER_ID_FIELD, userId),
                        eq(NAME_FIELD, id)))
                        .first());
    }

    @Override
    public List<Tag> getAll() {
        return collection.find(Filters.eq(USER_ID_FIELD, userId))
                .into(new ArrayList<>())
                .stream()
                .map(Documents::parseTag)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public Outcome insert(final Tag tag) {
        if (Objects.isNull(tag)) return Outcome.NULL;
        if (get(tag.getName()).isPresent()) return Outcome.UNIQUE_EXISTS;
        var opDoc = Documents.tagToDocument(tag);
        if (opDoc.isEmpty()) return Outcome.ERROR;
        collection.insertOne(opDoc.get());
        return Outcome.SUCCESS;
    }
}
