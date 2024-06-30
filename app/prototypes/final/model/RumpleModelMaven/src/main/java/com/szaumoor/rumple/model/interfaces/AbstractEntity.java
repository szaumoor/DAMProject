package com.szaumoor.rumple.model.interfaces;


import java.util.Objects;

/**
 * Skeletal implementation of the entities in the database
 * @param <T> The tye of the ID field
 */
public abstract class AbstractEntity<T> implements Entity {
    private T id;

    protected AbstractEntity(){}

    protected AbstractEntity(final T id){
        this.id = id;
    }

    public T getId() {
        return id;
    }

    public boolean setId(T id) {
        boolean valid = Objects.nonNull(id);
        if (valid) this.id = id;
        return valid;
    }
}