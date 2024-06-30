package com.rumpel.rumpeldesktop.fxutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class handles internationalization of text.
 */
public enum Translator {
    ;
    private static final String TR_FILE = "l10n";
    private static final ResourceBundle bundle;
    private static final Logger logger = LogManager.getLogger();

    static {
        bundle = ResourceBundle.getBundle(TR_FILE);
    }

    public static String getString(final String code) {
        String response = null;
        try {
            response = bundle.getString(code);
        } catch (final MissingResourceException ex) {
            logger.error("Missing resource: " + ex.getMessage());
        }
        return response;
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

}
