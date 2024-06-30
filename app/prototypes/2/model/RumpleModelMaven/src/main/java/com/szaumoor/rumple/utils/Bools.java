package com.szaumoor.rumple.utils;

import java.util.Arrays;

public enum Bools {
    ;
    public static boolean allTrue(final boolean b1, final boolean b2) {
        return b1 && b2;
    }

    public static boolean allTrue(final boolean b1, final boolean b2, final boolean b3) {
        return b1 && b2 && b3;
    }

    public static boolean allTrue(final boolean b1, final boolean b2, final boolean b3, final boolean b4) {
        return b1 && b2 && b3 && b4;
    }

    public static boolean allTrue(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5) {
        return b1 && b2 && b3 && b4 && b5;
    }

    public static boolean allTrue(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5, final boolean b6) {
        return b1 && b2 && b3 && b4 && b5 && b6;
    }

    public static boolean allTrue(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5,
                                  final boolean b6, final boolean b7) {
        return b1 && b2 && b3 && b4 && b5 && b6 && b7;
    }

    public static boolean allTrue(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5,
                                  final boolean b6, final boolean b7, final boolean b8) {
        return b1 && b2 && b3 && b4 && b5 && b6 && b7 && b8;
    }

    public static boolean allTrue(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5,
                                  final boolean b6, final boolean b7, final boolean b8, final boolean b9) {
        return b1 && b2 && b3 && b4 && b5 && b6 && b7 && b8 && b9;
    }

    public static boolean allTrue(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5,
                                  final boolean b6, final boolean b7, final boolean b8, final boolean b9, final boolean b10) {
        return b1 && b2 && b3 && b4 && b5 && b6 && b7 && b8 && b9 && b10;
    }

    public static boolean allTrue(final Boolean ... bools) {
        return Arrays.stream(bools).allMatch(aBoolean -> aBoolean);
    }

    public static boolean allFalse(final boolean b1, final boolean b2) {
        return !allTrue(b1, b2);
    }

    public static boolean allFalse(final boolean b1, final boolean b2, final boolean b3) {
        return !allTrue(b1, b2, b3);
    }

    public static boolean allFalse(final boolean b1, final boolean b2, final boolean b3, final boolean b4) {
        return !allTrue(b1, b2, b3, b4);
    }

    public static boolean allFalse(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5) {
        return !allTrue(b1, b2, b3, b4, b5);
    }

    public static boolean allFalse(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5, final boolean b6) {
        return !allTrue(b1, b2, b3, b4, b5, b6);
    }

    public static boolean allFalse(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5,
                                  final boolean b6, final boolean b7) {
        return !allTrue(b1, b2, b3, b4, b5, b6, b7);
    }

    public static boolean allFalse(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5,
                                  final boolean b6, final boolean b7, final boolean b8) {
        return !allTrue(b1, b2, b3, b4, b5, b6, b7, b8);
    }

    public static boolean allFalse(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5,
                                  final boolean b6, final boolean b7, final boolean b8, final boolean b9) {
        return !allTrue(b1, b2, b3, b4, b5, b6, b7, b8, b9);
    }

    public static boolean allFalse(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5,
                                  final boolean b6, final boolean b7, final boolean b8, final boolean b9, final boolean b10) {
        return !allTrue(b1, b2, b3, b4, b5, b6, b7, b8, b9, b10);
    }

    public static boolean allFalse(final Boolean ... bools) {
        return Arrays.stream(bools).noneMatch(aBoolean -> aBoolean);
    }

    public static boolean anyFalse(final boolean b1, final boolean b2) {
        return !b1 || !b2;
    }

    public static boolean anyFalse(final boolean b1, final boolean b2, final boolean b3) {
        return !b1 || !b2 || !b3;
    }

    public static boolean anyFalse(final boolean b1, final boolean b2, final boolean b3, final boolean b4) {
        return !b1 || !b2 || !b3 || !b4;
    }

    public static boolean anyFalse(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5) {
        return !b1 || !b2 || !b3 || !b4 || !b5;
    }

    public static boolean anyFalse(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5, final boolean b6) {
        return !b1 || !b2 || !b3 || !b4 || !b5 || !b6;
    }

    public static boolean anyFalse(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5,
                                   final boolean b6, final boolean b7) {
        return !b1 || !b2 || !b3 || !b4 || !b5 || !b6 || !b7;
    }

    public static boolean anyFalse(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5,
                                   final boolean b6, final boolean b7, final boolean b8) {
        return !b1 || !b2 || !b3 || !b4 || !b5 || !b6 || !b7 || !b8;
    }

    public static boolean anyFalse(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5,
                                   final boolean b6, final boolean b7, final boolean b8, final boolean b9) {
        return !b1 || !b2 || !b3 || !b4 || !b5 || !b6 || !b7 || !b8 || !b9;
    }

    public static boolean anyFalse(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5,
                                   final boolean b6, final boolean b7, final boolean b8, final boolean b9, final boolean b10) {
        return !b1 || !b2 || !b3 || !b4 || !b5 || !b6 || !b7 || !b8 || !b9 || !b10;
    }

    public static boolean anyFalse(final Boolean ... bools) {
        return Arrays.stream(bools).anyMatch(aBoolean -> !aBoolean);
    }

    public static boolean anyTrue(final boolean b1, final boolean b2) {
        return b1 || b2;
    }

    public static boolean anyTrue(final boolean b1, final boolean b2, final boolean b3) {
        return b1 || b2 || b3;
    }

    public static boolean anyTrue(final boolean b1, final boolean b2, final boolean b3, final boolean b4) {
        return b1 || b2 || b3 || b4;
    }

    public static boolean anyTrue(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5) {
        return b1 || b2 || b3 || b4 || b5;
    }

    public static boolean anyTrue(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5, final boolean b6) {
        return b1 || b2 || b3 || b4 || b5 || b6;
    }

    public static boolean anyTrue(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5,
                                   final boolean b6, final boolean b7) {
        return b1 || b2 || b3 || b4 || b5 || b6 || b7;
    }

    public static boolean anyTrue(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5,
                                   final boolean b6, final boolean b7, final boolean b8) {
        return b1 || b2 || b3 || b4 || b5 || b6 || b7 || b8;
    }

    public static boolean anyTrue(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5,
                                   final boolean b6, final boolean b7, final boolean b8, final boolean b9) {
        return b1 || b2 || b3 || b4 || b5 || b6 || b7 || b8 || b9;
    }

    public static boolean anyTrue(final boolean b1, final boolean b2, final boolean b3, final boolean b4, final boolean b5,
                                   final boolean b6, final boolean b7, final boolean b8, final boolean b9, final boolean b10) {
        return b1 || b2 || b3 || b4 || b5 || b6 || b7 || b8 || b9 || b10;
    }

    public static boolean anyTrue(final Boolean ... bools) {
        return Arrays.stream(bools).anyMatch(aBoolean -> aBoolean);
    }
}
