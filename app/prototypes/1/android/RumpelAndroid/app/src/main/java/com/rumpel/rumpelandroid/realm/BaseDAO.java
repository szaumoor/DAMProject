package com.rumpel.rumpelandroid.realm;

import com.szaumoor.rumple.model.interfaces.AbstractEntity;

import java.util.Optional;

interface BaseDAO<T extends AbstractEntity<Object>> {
    Optional<T> get(Object id);
    Outcome insert(T entity);
    Outcome modify(T entity);
}
