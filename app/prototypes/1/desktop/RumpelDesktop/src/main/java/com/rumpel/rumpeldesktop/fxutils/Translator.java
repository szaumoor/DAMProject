package com.rumpel.rumpeldesktop.fxutils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class handles internationalization of text.
 */
public enum Translator {
    ;
    private static final String TR_FILE = "l10n";
    private static final ResourceBundle bundle;

    static {
        bundle = ResourceBundle.getBundle(TR_FILE);
    }

    public static String getString(final String code) {
        String response = null;
        try {
            response = bundle.getString(code);
        } catch (final MissingResourceException ex) {
            System.err.println("Missing resource: " + ex.getMessage());
        }
        return response;
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

}
