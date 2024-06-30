package com.szaumoor.rumple.model.interfaces;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface Streamable<T> {
    Stream<T> stream();
    boolean add(final T item);
    boolean addAll(final Collection<T> elements);
    T getItem(final int index);
    int size();
    boolean contains(final T object);
    void forEach(final Consumer<T> consumer);
    void clear();
}
