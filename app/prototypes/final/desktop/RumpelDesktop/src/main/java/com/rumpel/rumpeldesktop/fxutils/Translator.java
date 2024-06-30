package com.rumpel.rumpeldesktop.fxutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class handles internationalization of strings.
 */
public final class Translator {
    /**
     * Private constructor to prevent instantiation
     */
    private Translator() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    private static final String TR_FILE = "l10n"; // misspelled, won't change in case something goes horribly wrong
    private static final ResourceBundle bundle;
    private static final Logger logger = LogManager.getLogger();

    static {
        bundle = ResourceBundle.getBundle(TR_FILE);
    }

    /**
     * Gets a string from the resource bundle based on the given code.
     *
     * @param code the code
     * @return the string associated with the code
     */
    public static String getString(final String code) {
        String response;
        try {
            response = bundle.getString(code);
        } catch (final MissingResourceException ex) {
            logger.error("Missing resource: " + ex.getMessage());
            return "???";
        }
        return response;
    }

    /**
     * Getter for the resource bundle
     *
     * @return the resource bundle
     */
    public static ResourceBundle getBundle() {
        return bundle;
    }

}
