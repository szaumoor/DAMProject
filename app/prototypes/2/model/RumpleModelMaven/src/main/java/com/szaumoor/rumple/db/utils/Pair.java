package com.szaumoor.rumple.db.utils;

public final class Pair<T, K> {
    private final T first;
    private final K second;

    public Pair(final T first, final K second) {
        this.first = first;
        this.second = second;
    }

    public static<T,K> Pair<T,K> of(T first, K second) {
        return new Pair<>(first, second);
    }

    public T first() {
        return first;
    }

    public K second() {
        return second;
    }
}