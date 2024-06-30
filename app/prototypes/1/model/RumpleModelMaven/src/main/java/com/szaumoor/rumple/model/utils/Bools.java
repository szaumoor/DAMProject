package com.szaumoor.rumple.model.utils;


public enum Bools {
    ;

    public static boolean allFalse(final boolean b1, final boolean b2) {
        return !b1 && !b2;
    }

    public static boolean allFalse(final boolean b1, final boolean b2, final boolean b3) {
        return !b1 && !b2 && !b3;
    }

    public static boolean anyFalse(final boolean b1, final boolean b2) {
        return !b1 || !b2;
    }

    public static boolean anyFalse(final boolean b1, final boolean b2, final boolean b3) {
        return !b1 || !b2 || !b3;
    }

}
