package com.szaumoor.rumple.model.utils;

import java.util.Arrays;
import java.util.Objects;

public enum Types {
    ;

    // NON NULL CHECKING METHODS

    public static boolean nonNull(final Object obj1, final Object obj2) {
        return obj1 != null && obj2 != null;
    }

    public static boolean nonNull(final Object obj1, final Object obj2, final Object obj3) {
        return obj1 != null && obj2 != null & obj3 != null;
    }

    public static boolean nonNull(final Object obj1, final Object obj2, final Object obj3, final Object obj4, final Object obj5) {
        return obj1 != null && obj2 != null & obj3 != null && obj4 != null && obj5 != null;
    }

    public static boolean nonNull(final Object obj1, final Object obj2, final Object obj3, final Object obj4, final Object obj5,
                                  final Object obj6) {
        return obj1 != null && obj2 != null & obj3 != null && obj4 != null && obj5 != null && obj6 != null;
    }

    public static boolean nonNull(final Object obj1, final Object obj2, final Object obj3, final Object obj4, final Object obj5,
                                  final Object obj6, final Object obj7) {
        return obj1 != null && obj2 != null & obj3 != null && obj4 != null && obj5 != null && obj6 != null && obj7 != null;
    }

    public static boolean nonNull(final Object obj1, final Object obj2, final Object obj3, final Object obj4, final Object obj5,
                                  final Object obj6, final Object obj7, final Object obj8) {
        return obj1 != null && obj2 != null & obj3 != null && obj4 != null && obj5 != null && obj6 != null && obj7 != null &&
                obj8 != null;
    }

    public static boolean nonNull(final Object obj1, final Object obj2, final Object obj3, final Object obj4, final Object obj5,
                                  final Object obj6, final Object obj7, final Object obj8, final Object obj9) {
        return obj1 != null && obj2 != null & obj3 != null && obj4 != null && obj5 != null && obj6 != null && obj7 != null &&
                obj8 != null && obj9 != null;
    }

    public static boolean nonNull(final Object obj1, final Object obj2, final Object obj3, final Object obj4, final Object obj5,
                                  final Object obj6, final Object obj7, final Object obj8, final Object obj9, final Object obj10) {
        return obj1 != null && obj2 != null & obj3 != null && obj4 != null && obj5 != null && obj6 != null && obj7 != null &&
                obj8 != null && obj9 != null && obj10 != null;
    }

    public static boolean nonNull(final Object... objs) {
        return Arrays.stream(objs).allMatch(Objects::nonNull);
    }

    // ANY NULL CHECKING METHODS

    public static boolean anyNull(final Object obj1, final Object obj2) {
        return obj1 == null || obj2 == null;
    }

    public static boolean anyNull(final Object obj1, final Object obj2, final Object obj3) {
        return obj1 == null || obj2 == null || obj3 == null;
    }

    public static boolean anyNull(final Object obj1, final Object obj2, final Object obj3, final Object obj4) {
        return obj1 == null || obj2 == null || obj3 == null || obj4 == null;
    }
}
