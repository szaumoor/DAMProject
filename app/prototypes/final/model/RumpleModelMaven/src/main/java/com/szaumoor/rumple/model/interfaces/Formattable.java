package com.szaumoor.rumple.model.interfaces;

/**
 * Interface for entities that can be formatted in a meaningful way
 * @param <T> The type of the format
 */
public interface Formattable<T> {
    T format();
}
