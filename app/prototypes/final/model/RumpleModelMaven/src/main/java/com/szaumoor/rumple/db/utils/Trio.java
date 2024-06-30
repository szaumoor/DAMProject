package com.szaumoor.rumple.db.utils;

/**
 * Utility record to hold a set of three values
 *
 * @param first  First element
 * @param second Second element
 * @param third  Third element
 * @param <T>    The type of the first element
 * @param <K>    The type of the second element
 * @param <S>    The type of the third element
 */
public record Trio<T, K, S>(T first, K second, S third) {

    /**
     * The static way of creating the record
     *
     * @param first  First element
     * @param second Second element
     * @param third  Third element
     * @param <T>    The type of the first element
     * @param <K>    The type of the second element
     * @param <S>    The type of the third element
     */
    public static <T, K, S> Trio<T, K, S> of(T first, K second, S third) {
        return new Trio<>(first, second, third);
    }
}
