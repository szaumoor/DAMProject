package com.szaumoor.rumple.model.interfaces;

import java.util.Collection;
import java.util.stream.Stream;

public interface Streamable<T> {
    Stream<T> stream();
    boolean addAll(final Collection<T> elements);

    T getItem(final int index);

    int size();
}
