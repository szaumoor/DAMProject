package com.rumpel.rumpeldesktop.db;

import com.szaumoor.rumple.db.PluralDAO;
import com.szaumoor.rumple.model.interfaces.Entity;

abstract class AbstractPluralDAO<T extends Entity> extends AbstractDAO implements PluralDAO<T> {
    protected AbstractPluralDAO(final String collName) {
        super(collName);
    }
}
