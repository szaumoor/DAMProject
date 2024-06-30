package com.rumpel.rumpeldesktop.db;

import com.szaumoor.rumple.db.BaseDAO;
import com.szaumoor.rumple.model.interfaces.Entity;

/**
 * Skeletal base DAO implementation for non-plural DAOs (currently only users).
 *
 * @param <T> Entity type
 */
abstract class AbstractBaseDAO<T extends Entity> extends AbstractDAO implements BaseDAO<T> {
    /**
     * Protected constructor that delegates the MongoDB connection to the superclass.
     *
     * @param collName Name of the collection
     */
    protected AbstractBaseDAO(final String collName) {
        super(collName);
    }
}
