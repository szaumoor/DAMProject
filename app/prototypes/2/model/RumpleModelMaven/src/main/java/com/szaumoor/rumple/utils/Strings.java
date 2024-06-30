package com.szaumoor.rumple.utils;

import com.szaumoor.rumple.utils.exceptions.NegativeLengthException;

import java.util.Arrays;

public enum Strings {
    ;

    /**
     * Verifies if a string reference is not null and has non-whitespace content.
     *
     * @param str String to check
     * @return True if it has content, false otherwise
     */
    public static boolean hasContent(final String str) {
        return str != null && !str.isBlank();
    }

    public static boolean hasContent(final String str1, final String str2) {
        return hasContent(str1) && hasContent(str2);
    }

    public static boolean hasContent(final String str1, final String str2, final String str3) {
        return hasContent(str1, str2) && hasContent(str3);
    }

    public static boolean hasContent(final String str1, final String str2, final String str3, final String str4) {
        return hasContent(str1, str2, str3) && hasContent(str4);
    }

    public static boolean hasContent(final String str1, final String str2, final String str3, final String str4, final String str5) {
        return hasContent(str1, str2, str3, str4) && hasContent(str5);
    }

    public static boolean hasContent(final String str1, final String str2, final String str3, final String str4, final String str5,
                                     final String str6) {
        return hasContent(str1, str2, str3, str4, str5) && hasContent(str6);
    }

    public static boolean hasContent(final String str1, final String str2, final String str3, final String str4, final String str5,
                                     final String str6, final String str7) {
        return hasContent(str1, str2, str3, str4, str5, str6) && hasContent(str7);
    }

    public static boolean hasContent(final String str1, final String str2, final String str3, final String str4, final String str5,
                                     final String str6, final String str7, final String str8) {
        return hasContent(str1, str2, str3, str4, str5, str6, str7) && hasContent(str8);
    }

    public static boolean hasContent(final String str1, final String str2, final String str3, final String str4, final String str5,
                                     final String str6, final String str7, final String str8, final String str9) {
        return hasContent(str1, str2, str3, str4, str5, str6, str7, str8) && hasContent(str9);
    }

    public static boolean hasContent(final String str1, final String str2, final String str3, final String str4, final String str5,
                                     final String str6, final String str7, final String str8, final String str9, final String str10) {
        return hasContent(str1, str2, str3, str4, str5, str6, str7, str8, str9) && hasContent(str10);
    }

    public static boolean hasContent(final String ... strings) {
        return Arrays.stream(strings).allMatch(Strings::hasContent);
    }

    public static boolean minOrAbove(final String str, final int min) {
        return str.length() >= min;
    }

    public static boolean aboveMin(final String str, final int min) {
        return str.length() > min;
    }

    public static boolean maxOrBelow(final String str, final int max) {
        return str.length() <= max;
    }

    public static boolean belowMax(final String str, final int max) {
        return str.length() < max;
    }

    public static boolean between(final String str, final int min, final int max) {
        if (min < 0) throw new NegativeLengthException("Minimum length of String cannot be negative");
        if (max <= min)
            throw new IllegalArgumentException("Maximum length of String cannot be less or equal to its minimum length");
        int length = str.length();
        return length >= min && length <= max;
    }
}
