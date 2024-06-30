package com.rumpel.rumpeldesktop.db;

import com.rumpel.rumpeldesktop.db.utils.Documents;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.Tag;
import org.bson.BsonObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.*;
import static com.szaumoor.rumple.model.interfaces.Entity.ID_FIELD;
import static com.szaumoor.rumple.model.interfaces.Entity.USER_ID_FIELD;

/**
 * DAO class that handles PaymentMethod entities and objects.
 */
public final class DAOTag extends AbstractPluralDAO<Tag> {

    private final BsonObjectId userId;

    /**
     * No-args constructor initializing the basic fields and retrieves the collection.
     */
    public DAOTag() {
        super(Tag.COLL_TAGS);
        userId = (BsonObjectId) DAOUser.getLoggedUser().getId();
    }

    /**
     * Retrieves an optional Tag object based on the provided ID.
     *
     * @param id the ID of the Tag object to retrieve
     * @return an optional Tag object if found, otherwise an empty optional
     */
    @Override
    public Optional<Tag> get(final Object id) {
        return crudGet(eq(Tag.NAME_FIELD, id.toString()), DAOUser.getLoggedUser(), Documents::parseTag);
    }

    /**
     * Retrieves all the tags of an user.
     *
     * @return a list of Tag objects
     */
    @Override
    public List<Tag> getAll() {
        return crudGetAll(DAOUser.getLoggedUser(), Documents::parseTag);
    }

    /**
     * Retrieves a list of tags by their IDs.
     *
     * @param ids a list of tag IDs
     * @return a list of Tag objects
     */
    @Override
    public List<Tag> getAllById(final List<Object> ids) {
        return collection.find(and(eq(USER_ID_FIELD, userId), in(ID_FIELD, ids)))
                .map(Documents::parseTag)
                .map(Optional::orElseThrow)
                .into(new ArrayList<>());
    }

    /**
     * Inserts all the given tags into the list.
     * <br><br> Unsupported. There was no need so far to use it.
     *
     * @param list the list of tags to insert
     * @return an Outcome object indicating the result of the insertion
     */
    @Override
    public Outcome insertAll(final List<Tag> list) {
        throw new UnsupportedOperationException();
    }

    /**
     * Deletes all elements in the list.
     * <br><br> Unsupported. There was no need so far to use it.
     *
     * @param list the list of tags to be deleted
     * @return an Outcome object indicating the result of the operation
     */
    @Override
    public Outcome deleteAll(final List<Tag> list) {
        throw new UnsupportedOperationException();
    }

    /**
     * Deletes all records.
     *
     * @return the outcome of the delete operation
     */
    @Override
    public Outcome deleteAll() {
        return crudDeleteAll(DAOUser.getLoggedUser(), Documents::parseTag);
    }

    /**
     * Inserts a new tag into the system.
     *
     * @param tag the tag to be inserted
     * @return the outcome of the insertion
     */
    @Override
    public Outcome insert(final Tag tag) {
        return crudInsert(tag, () -> get(tag.getName()).isPresent(), Documents::tagToDocument);
    }

    /**
     * Modify the given tag.
     *
     * @param tag the tag to modify
     * @return the outcome of the modification
     */
    @Override
    public Outcome modify(final Tag tag) {
        return crudModify(tag, tag.getUser(), Documents::tagToDocument);
    }

    /**
     * Deletes a tag.
     *
     * @param tag the tag object to delete
     * @return the outcome of the delete operation
     */
    @Override
    public Outcome delete(final Tag tag) {
        return crudDelete(tag, Documents::tagToDocument);
    }

    /**
     * Retrieves the Tag object with the specified ID.
     * <br><br> Unsupported. There was no need so far to use it.
     *
     * @param id the ID of the Tag object to retrieve
     * @return an Optional containing the Tag object if found, otherwise empty
     */
    @Override
    public Optional<Tag> getById(final Object id) {
        throw new UnsupportedOperationException();
    }
}
