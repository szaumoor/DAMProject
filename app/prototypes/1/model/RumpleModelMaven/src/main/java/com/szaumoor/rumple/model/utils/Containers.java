package com.szaumoor.rumple.model.utils;

import com.szaumoor.rumple.model.utils.exceptions.NegativeLengthException;

public enum Containers {
    ;

    public static boolean between(final char[] array, final int min, final int max) {
        if (min < 0) throw new NegativeLengthException("Minimum length of array cannot be negative");
        if (max <= min)
            throw new IllegalArgumentException("Maximum length of array cannot be less or equal to its minimum length");
        final int size = array.length;
        return size >= min && size <= max;
    }

    public static <T> boolean between(final T[] array, final int min, final int max) {
        if (min < 0) throw new NegativeLengthException("Minimum length of array cannot be negative");
        if (max <= min)
            throw new IllegalArgumentException("Maximum length of array cannot be less or equal to its minimum length");
        final int size = array.length;
        return size >= min && size <= max;
    }
}
