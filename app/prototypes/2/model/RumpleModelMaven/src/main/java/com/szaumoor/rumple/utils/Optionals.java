package com.szaumoor.rumple.utils;

import java.util.Arrays;
import java.util.Optional;

public enum Optionals {
    ;

    public static boolean allPresent(final Optional<?> op1, final Optional<?> op2) {
        return op1.isPresent() && op2.isPresent();
    }

    public static boolean allPresent(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3) {
        return op1.isPresent() && op2.isPresent() && op3.isPresent();
    }

    public static boolean allPresent(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4) {
        return op1.isPresent() && op2.isPresent() && op3.isPresent() && op4.isPresent();
    }

    public static boolean allPresent(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5) {
        return op1.isPresent() && op2.isPresent() && op3.isPresent() && op4.isPresent() && op5.isPresent();
    }

    public static boolean allPresent(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5,
                                     final Optional<?> op6) {
        return op1.isPresent() && op2.isPresent() && op3.isPresent() && op4.isPresent() && op5.isPresent() && op6.isPresent();
    }

    public static boolean allPresent(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5,
                                     final Optional<?> op6, final Optional<?> op7) {
        return op1.isPresent() && op2.isPresent() && op3.isPresent() && op4.isPresent() && op5.isPresent() && op6.isPresent() && op7.isPresent();
    }

    public static boolean allPresent(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5,
                                     final Optional<?> op6, final Optional<?> op7, final Optional<?> op8) {
        return op1.isPresent() && op2.isPresent() && op3.isPresent() && op4.isPresent() && op5.isPresent() && op6.isPresent() && op7.isPresent() &&
                op8.isPresent();
    }

    public static boolean allPresent(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5,
                                     final Optional<?> op6, final Optional<?> op7, final Optional<?> op8, final Optional<?> op9) {
        return op1.isPresent() && op2.isPresent() && op3.isPresent() && op4.isPresent() && op5.isPresent() && op6.isPresent() && op7.isPresent() &&
                op8.isPresent() && op9.isPresent();
    }

    public static boolean allPresent(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5,
                                     final Optional<?> op6, final Optional<?> op7, final Optional<?> op8, final Optional<?> op9, final Optional<?> op10) {
        return op1.isPresent() && op2.isPresent() && op3.isPresent() && op4.isPresent() && op5.isPresent() && op6.isPresent() && op7.isPresent() &&
                op8.isPresent() && op9.isPresent() && op10.isPresent();
    }

    public static boolean allPresent(final Optional<?> ... optionals) {
        return Arrays.stream(optionals).allMatch(Optional::isPresent);
    }

    public static boolean anyPresent(final Optional<?> op1, final Optional<?> op2) {
        return op1.isPresent() || op2.isPresent();
    }

    public static boolean anyPresent(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3) {
        return op1.isPresent() || op2.isPresent() || op3.isPresent();
    }

    public static boolean anyPresent(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4) {
        return op1.isPresent() || op2.isPresent() || op3.isPresent() || op4.isPresent();
    }

    public static boolean anyPresent(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5) {
        return op1.isPresent() || op2.isPresent() || op3.isPresent() || op4.isPresent() || op5.isPresent();
    }

    public static boolean anyPresent(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5,
                                     final Optional<?> op6) {
        return op1.isPresent() || op2.isPresent() || op3.isPresent() || op4.isPresent() || op5.isPresent() || op6.isPresent();
    }

    public static boolean anyPresent(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5,
                                     final Optional<?> op6, final Optional<?> op7) {
        return op1.isPresent() || op2.isPresent() || op3.isPresent() || op4.isPresent() || op5.isPresent() || op6.isPresent() || op7.isPresent();
    }

    public static boolean anyPresent(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5,
                                     final Optional<?> op6, final Optional<?> op7, final Optional<?> op8) {
        return op1.isPresent() && op2.isPresent() && op3.isPresent() && op4.isPresent() && op5.isPresent() && op6.isPresent() && op7.isPresent() &&
                op8.isPresent();
    }

    public static boolean anyPresent(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5,
                                     final Optional<?> op6, final Optional<?> op7, final Optional<?> op8, final Optional<?> op9) {
        return op1.isPresent() && op2.isPresent() && op3.isPresent() && op4.isPresent() && op5.isPresent() && op6.isPresent() && op7.isPresent() &&
                op8.isPresent() && op9.isPresent();
    }

    public static boolean anyPresent(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5,
                                     final Optional<?> op6, final Optional<?> op7, final Optional<?> op8, final Optional<?> op9, final Optional<?> op10) {
        return op1.isPresent() && op2.isPresent() && op3.isPresent() && op4.isPresent() && op5.isPresent() && op6.isPresent() && op7.isPresent() &&
                op8.isPresent() && op9.isPresent() && op10.isPresent();
    }

    public static boolean anyPresent(final Optional<?> ... optionals) {
        return Arrays.stream(optionals).anyMatch(Optional::isPresent);
    }

    public static boolean allEmpty(final Optional<?> op1, final Optional<?> op2) {
        return op1.isEmpty() && op2.isEmpty();
    }

    public static boolean allEmpty(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3) {
        return op1.isEmpty() && op2.isEmpty() && op3.isEmpty();
    }

    public static boolean allEmpty(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4) {
        return op1.isEmpty() && op2.isEmpty() && op3.isEmpty() && op4.isEmpty();
    }

    public static boolean allEmpty(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5) {
        return op1.isEmpty() && op2.isEmpty() && op3.isEmpty() && op4.isEmpty() && op5.isEmpty();
    }

    public static boolean allEmpty(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5,
                                     final Optional<?> op6) {
        return op1.isEmpty() && op2.isEmpty() && op3.isEmpty() && op4.isEmpty() && op5.isEmpty() && op6.isEmpty();
    }

    public static boolean allEmpty(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5,
                                     final Optional<?> op6, final Optional<?> op7) {
        return op1.isEmpty() && op2.isEmpty() && op3.isEmpty() && op4.isEmpty() && op5.isEmpty() && op6.isEmpty() && op7.isEmpty();
    }

    public static boolean allEmpty(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5,
                                     final Optional<?> op6, final Optional<?> op7, final Optional<?> op8) {
        return op1.isEmpty() && op2.isEmpty() && op3.isEmpty() && op4.isEmpty() && op5.isEmpty() && op6.isEmpty() && op7.isEmpty() &&
                op8.isEmpty();
    }

    public static boolean allEmpty(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5,
                                     final Optional<?> op6, final Optional<?> op7, final Optional<?> op8, final Optional<?> op9) {
        return op1.isEmpty() && op2.isEmpty() && op3.isEmpty() && op4.isEmpty() && op5.isEmpty() && op6.isEmpty() && op7.isEmpty() &&
                op8.isEmpty() && op9.isEmpty();
    }

    public static boolean allEmpty(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5,
                                     final Optional<?> op6, final Optional<?> op7, final Optional<?> op8, final Optional<?> op9, final Optional<?> op10) {
        return op1.isEmpty() && op2.isEmpty() && op3.isEmpty() && op4.isEmpty() && op5.isEmpty() && op6.isEmpty() && op7.isEmpty() &&
                op8.isEmpty() && op9.isEmpty() && op10.isEmpty();
    }

    public static boolean allEmpty(final Optional<?> ... optionals) {
        return Arrays.stream(optionals).allMatch(Optional::isEmpty);
    }


    public static boolean anyEmpty(final Optional<?> op1, final Optional<?> op2) {
        return op1.isEmpty() || op2.isEmpty();
    }

    public static boolean anyEmpty(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3) {
        return op1.isEmpty() || op2.isEmpty() || op3.isEmpty();
    }

    public static boolean anyEmpty(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4) {
        return op1.isEmpty() || op2.isEmpty() || op3.isEmpty() || op4.isEmpty();
    }

    public static boolean anyEmpty(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4,  final Optional<?> op5) {
        return op1.isEmpty() || op2.isEmpty() || op3.isEmpty() || op4.isEmpty() || op5.isEmpty();
    }

    public static boolean anyEmpty(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5,
                                     final Optional<?> op6) {
        return op1.isEmpty() || op2.isEmpty() || op3.isEmpty() || op4.isEmpty() || op5.isEmpty() || op6.isEmpty();
    }

    public static boolean anyEmpty(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5,
                                     final Optional<?> op6, final Optional<?> op7) {
        return op1.isEmpty() || op2.isEmpty() || op3.isEmpty() || op4.isEmpty() || op5.isEmpty() || op6.isEmpty() || op7.isEmpty();
    }

    public static boolean anyEmpty(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5,
                                     final Optional<?> op6, final Optional<?> op7, final Optional<?> op8) {
        return op1.isEmpty() && op2.isEmpty() && op3.isEmpty() && op4.isEmpty() && op5.isEmpty() && op6.isEmpty() && op7.isEmpty() &&
                op8.isEmpty();
    }

    public static boolean anyEmpty(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5,
                                     final Optional<?> op6, final Optional<?> op7, final Optional<?> op8, final Optional<?> op9) {
        return op1.isEmpty() && op2.isEmpty() && op3.isEmpty() && op4.isEmpty() && op5.isEmpty() && op6.isEmpty() && op7.isEmpty() &&
                op8.isEmpty() && op9.isEmpty();
    }

    public static boolean anyEmpty(final Optional<?> op1, final Optional<?> op2, final Optional<?> op3, final Optional<?> op4, final Optional<?> op5,
                                     final Optional<?> op6, final Optional<?> op7, final Optional<?> op8, final Optional<?> op9, final Optional<?> op10) {
        return op1.isEmpty() && op2.isEmpty() && op3.isEmpty() && op4.isEmpty() && op5.isEmpty() && op6.isEmpty() && op7.isEmpty() &&
                op8.isEmpty() && op9.isEmpty() && op10.isEmpty();
    }

    public static boolean anyEmpty(final Optional<?> ... optionals) {
        return Arrays.stream(optionals).anyMatch(Optional::isEmpty);
    }

}
