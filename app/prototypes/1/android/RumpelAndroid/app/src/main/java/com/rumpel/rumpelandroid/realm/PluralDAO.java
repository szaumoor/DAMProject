package com.rumpel.rumpelandroid.realm;

import com.szaumoor.rumple.model.interfaces.AbstractEntity;

import java.util.List;

interface PluralDAO<T extends AbstractEntity<Object>> extends BaseDAO<T> {
    List<T> getAll();
    Outcome insertAll(final List<T> elements);
}
