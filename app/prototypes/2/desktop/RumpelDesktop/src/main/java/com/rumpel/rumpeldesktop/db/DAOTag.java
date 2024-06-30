package com.rumpel.rumpeldesktop.db;

import com.mongodb.client.model.Filters;
import com.rumpel.rumpeldesktop.db.utils.Documents;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.Tag;
import org.bson.BsonObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.szaumoor.rumple.model.interfaces.Entity.ID_FIELD;
import static com.szaumoor.rumple.model.interfaces.Entity.USER_ID_FIELD;

public final class DAOTag extends AbstractPluralDAO<Tag> {

    private final BsonObjectId userId;

    public DAOTag() {
        super(Tag.COLL_TAGS);
        userId = (BsonObjectId) DAOUser.getLoggedUser().getId();
    }

    @Override
    public Optional<Tag> get(final Object id) {
        return crudGet(eq(Tag.NAME_FIELD, id.toString()), DAOUser.getLoggedUser(), Documents::parseTag);
    }

    @Override
    public List<Tag> getAll() {
        return crudGetAll(DAOUser.getLoggedUser(), Documents::parseTag);
    }

    @Override
    public List<Tag> getAllById(final List<Object> ids) {
        return collection.find(
                and(
                        Filters.eq(USER_ID_FIELD, userId),
                        Filters.in(ID_FIELD, ids)
                )
        ).map(Documents::parseTag)
                .map(Optional::orElseThrow)
                .into(new ArrayList<>());

    }

    @Override
    public Outcome insertAll(final List<Tag> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Outcome deleteAll(final List<Tag> list) {
        return null;
    }

    @Override
    public Outcome deleteAll() {
        return crudDeleteAll(DAOUser.getLoggedUser(), Documents::parseTag);
    }

    @Override
    public Outcome insert(final Tag tag) {
        return crudInsert(tag, () -> get(tag.getName()).isPresent(), Documents::tagToDocument);
    }

    @Override
    public Outcome modify(final Tag tag) {
        return crudModify(tag, tag.getUser(), Documents::tagToDocument);
    }

    @Override
    public Outcome delete(final Tag tag) {
        return crudDelete(tag, Documents::tagToDocument, null);
    }

    @Override
    public Optional<Tag> getById(final Object id) {
        throw new UnsupportedOperationException();
    }
}
