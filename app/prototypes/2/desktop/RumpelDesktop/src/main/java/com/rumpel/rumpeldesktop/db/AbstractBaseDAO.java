package com.rumpel.rumpeldesktop.db;

import com.szaumoor.rumple.db.BaseDAO;
import com.szaumoor.rumple.model.interfaces.Entity;

abstract class AbstractBaseDAO<T extends Entity> extends AbstractDAO implements BaseDAO<T> {
    protected AbstractBaseDAO(final String collName) {
        super(collName);
    }
}
