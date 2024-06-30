package com.szaumoor.rumple.db.utils;

/**
 * Utility record to hold a pair of values
 *
 * @param first  First element
 * @param second Second element
 * @param <T>    The type of the first element
 * @param <K>    The type of the second element
 */
public record Pair<T, K>(T first, K second) {

    /**
     * Static way of creating a pair.
     *
     * @param first  First element
     * @param second Second element
     * @param <T>    The type of the first element
     * @param <K>    The type of the second element
     * @return Resulting pair
     */
    public static <T, K> Pair<T, K> of(T first, K second) {
        return new Pair<>(first, second);
    }
}