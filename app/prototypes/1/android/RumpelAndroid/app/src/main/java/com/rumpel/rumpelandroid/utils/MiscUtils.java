package com.rumpel.rumpelandroid.utils;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Helper class with generic utilities for programming.
 */
public enum MiscUtils {
    ;

    /**
     * Returns true if every element passed is not null.
     *
     * @param objs Objects to check
     * @return True if all elements are not null, false otherwise
     */
    public static boolean allNonNull(final Object... objs) {
        return Stream.of(objs).allMatch(Objects::nonNull);
    }

    /**
     * Returns true if every element passed is null.
     *
     * @param objs Objects to check
     * @return True if all elements are null, false otherwise
     */
    public static boolean allNull(final Object... objs) {
        return Stream.of(objs).allMatch(Objects::isNull);
    }

    /**
     * Returns true if any element passed is null.
     *
     * @param objs Objects to check
     * @return True if any elements is null, false otherwise
     */
    public static boolean anyIsNull(final Object... objs) {
        return Stream.of(objs).anyMatch(Objects::isNull);
    }

    /**
     * Returns true if the passed object in the first parameter is
     * equal to any of the other passed objects.
     *
     * @param toCompareTo Object to look for equality
     * @param objs        Objects for comparison
     * @return True if the first object is equal to any of the rest, false otherwise
     */
    public static boolean equalToAny(Object toCompareTo, Object... objs) {
        return Arrays.asList(objs).contains(toCompareTo);
    }
}