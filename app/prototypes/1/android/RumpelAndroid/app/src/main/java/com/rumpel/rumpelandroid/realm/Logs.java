package com.rumpel.rumpelandroid.realm;

import android.util.Log;

public enum Logs {
    ;

    public static void interrupt(final String tag) {
        Log.e(tag, "Thread was interrupted unexpectedly while waiting for its termination");
    }
}
