package com.rumpel.rumpeldesktop.db;

import com.szaumoor.rumple.db.PluralDAO;
import com.szaumoor.rumple.model.interfaces.Entity;

/**
 * Skeletal base DAO implementation for plural DAOs (currently all except users).
 *
 * @param <T> Entity type
 */
abstract class AbstractPluralDAO<T extends Entity> extends AbstractDAO implements PluralDAO<T> {
    /**
     * Protected constructor that delegates the MongoDB connection to the superclass.
     *
     * @param collName Name of the collection
     */
    protected AbstractPluralDAO(final String collName) {
        super(collName);
    }

}
