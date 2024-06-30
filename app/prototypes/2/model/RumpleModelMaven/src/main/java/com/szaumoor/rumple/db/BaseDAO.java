package com.szaumoor.rumple.db;

import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.interfaces.Entity;

import java.util.Optional;

public interface BaseDAO<T extends Entity> {

    Optional<T> get(final Object id);
    Optional<T> getById(final Object id);
    Outcome insert(final T entity);
    Outcome modify(final T entity);
    Outcome delete(final T entity);
}
