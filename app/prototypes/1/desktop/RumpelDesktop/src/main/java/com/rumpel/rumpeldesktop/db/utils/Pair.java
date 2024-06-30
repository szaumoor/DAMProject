package com.rumpel.rumpeldesktop.db.utils;

public record Pair<T, K>(T first, K second) {

    public static<T,K> Pair<T,K> of(T first, K second) {
        return new Pair<>(first, second);
    }
}
