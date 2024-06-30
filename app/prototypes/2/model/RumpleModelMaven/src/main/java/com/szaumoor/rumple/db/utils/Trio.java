package com.szaumoor.rumple.db.utils;

public final class Trio <T, K, S>{
    private final T first;
    private final K second;
    private final S third;

    public Trio(T first, K second, S third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static <T, K, S> Trio<T,K,S> of(T first, K second, S third){
        return new Trio<>(first, second, third);
    }

    public T first() {
        return first;
    }

    public K second() {
        return second;
    }

    public S third() {
        return third;
    }
}
