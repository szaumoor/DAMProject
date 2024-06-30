package com.rumpel.rumpelandroid.utils;

import android.util.Log;

/**
 * Singleton class with static utility methods for logging predetermined messages in Android.
 */
public enum Logs {
    ;

    /**
     * Logs a thread timeout exception.
     * @param tag String defining the "tag" for this log. It should be the class the method call comes from.
     */
    public static void interrupt(final String tag) {
        Log.e(tag, "Thread was interrupted unexpectedly while waiting for its termination");
    }

    /**
     * Logs an object insertion error in the database.
     * @param tag String defining the "tag" for this log. It should be the class the method call comes from.
     */
    public static void insertionError(final String tag) {
        Log.e(tag, "Error inserting in the database!");
    }

    /**
     * Logs an unexpected object creation error.
     * @param tag String defining the "tag" for this log. It should be the class the method call comes from.
     */
    public static void creationError(final String tag, Object obj) {
        Log.e(tag, "An object failed to be created correctly unexpectedly: " + obj.getClass().getSimpleName());
    }
}
