package com.rumpel.rumpeldesktop.fxutils;

import java.util.Optional;

import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.errorAndWait;

public enum Dialogs {
    ;

    public static boolean error(final boolean bool, final String errorMsg) {
        if (bool) {
            errorAndWait(errorMsg);
            return true;
        }
        return false;
    }

    public static boolean error(final Optional<?> optional, final String errorMsg) {
        return error(optional.isEmpty(), errorMsg);
    }
}
