package com.szaumoor.rumple.db;

import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.interfaces.Entity;

import java.util.List;

/**
 * Interface containing common database operations to retrieve more than one element
 *
 * @param <T> The type of the database entity
 */
public interface PluralDAO<T extends Entity> extends BaseDAO<T> {
    List<T> getAll();

    List<T> getAllById(final List<Object> ids);

    Outcome insertAll(final List<T> elements);

    Outcome deleteAll(final List<T> elements);

    Outcome deleteAll();
}
