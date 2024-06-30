package com.rumpel.rumpelandroid.utils;

/**
 * Helper class with utilities related to Strings.
 */
public enum StringUtils {
    ;
    /**
     * Checks whether a meaningful String is correct for the purposes of this app,
     * that is, it must be not null and contain characters other than whitespaces.
     *
     * @param toTest String to test.
     * @return True if the passed string is not null and has character
     * content (ignoring whitespaces), false otherwise.
     */
    public static boolean stringHasContent(final String toTest) {
        return toTest != null && !toTest.isBlank();
    }
}
