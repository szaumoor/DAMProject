package com.szaumoor.rumple.model.utils;

import com.szaumoor.rumple.model.utils.exceptions.NegativeLengthException;

public enum Strings {
    ;

    /**
     * Checks if a string reference is not null and has non-whitespace content.
     *
     * @param str String to check
     * @return True if it has content, false otherwise
     */
    public static boolean hasContent(final String str) {
        return str != null && !str.isBlank();
    }

    public static boolean isMin(final String str, final int min) {
        return str.length() >= min;
    }

    public static boolean between(final String str, final int min, final int max) {
        if (min < 0) throw new NegativeLengthException("Minimum length of String cannot be negative");
        if (max <= min)
            throw new IllegalArgumentException("Maximum length of String cannot be less or equal to its minimum length");
        int length = str.length();
        return length >= min && length <= max;
    }
}
