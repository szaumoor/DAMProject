package com.rumpel.rumpelandroid.db;

import static com.rumpel.rumpelandroid.db.utils.Filters.eq;
import static com.szaumoor.rumple.model.interfaces.Entity.ID_FIELD;

import android.content.Context;

import com.rumpel.rumpelandroid.db.utils.Documents;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.Tag;

import java.util.List;
import java.util.Optional;
/**
 * DAO class that handles PaymentMethod entities and objects.
 */
public class DAOTags extends AbstractPluralDAO<Tag> {
    /**
     * Constructor that initializes the basic fields and retrieves the collection.
     */
    public DAOTags(final Context context) {
        super(context, Tag.COLL_TAGS);
    }
    /**
     * Retrieves an optional Tag object based on the provided ID.
     *
     * @param id the ID of the Tag object to retrieve
     * @return an optional Tag object if found, otherwise an empty optional
     */
    @Override
    public Optional<Tag> get(final Object id) {
        return crudGet(eq(Tag.NAME_FIELD, id.toString()), Documents::parseTag, false);
    }
    /**
     * Retrieves the Tag object with the specified ID.
     *
     * @param id the ID of the Tag object to retrieve
     * @return an Optional containing the Tag object if found, otherwise empty
     */
    @Override
    public Optional<Tag> getById(final Object id) {
        return crudGet(eq(ID_FIELD, id), Documents::parseTag, false);
    }
    /**
     * Retrieves all the tags of an user.
     *
     * @return a list of Tag objects
     */
    @Override
    public List<Tag> getAll() {
        return crudGetAll(Documents::parseTag);
    }
    /**
     * Retrieves a list of tags by their IDs.
     *
     * @param ids a list of tag IDs
     * @return a list of Tag objects
     */
    @Override
    public List<Tag> getAllById(final List<Object> ids) {
        return crudGetAllById(Documents::parseTag, ids);
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

    @Override // unused for now
    public Outcome deleteAll(final List<Tag> list) {
        throw new UnsupportedOperationException();
    }

    @Override //unused for now
    public Outcome deleteAll() {
        throw new UnsupportedOperationException();
    }
    /**
     * Inserts a new tag into the system.
     *
     * @param tag the tag to be inserted
     * @return the outcome of the insertion
     */
    @Override
    public Outcome insert(final Tag tag) {
        return crudInsert(tag, () -> getById(tag.getId()).isPresent(), Documents::tagToDocument);
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
     * Modify the given tag.
     *
     * @param tag the tag to modify
     * @return the outcome of the modification
     */
    @Override
    public Outcome modify(final Tag tag) {
        return crudModify(tag, Documents::tagToDocument);
    }
}
