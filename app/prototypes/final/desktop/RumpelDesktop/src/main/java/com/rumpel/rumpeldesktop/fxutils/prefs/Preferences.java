package com.rumpel.rumpeldesktop.fxutils.prefs;

import com.szaumoor.utils.Property;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Currency;
import java.util.Locale;
import java.util.prefs.BackingStoreException;

/**
 * Singleton representing a Preferences object holding the methods to access and set settings of the program.
 * Saves the default settings in the registry upon first loading it.
 */
public enum Preferences {
    instance;

    private final java.util.prefs.Preferences prefs;
    private static final Logger logger = LogManager.getLogger(Preferences.class);
    public static final String THEME_KEY = "theme";
    public static final String LANGUAGE_KEY = "language";
    public static final String DEFAULT_CURRENCY_KEY = "default-currency";
    public static final String DEFAULT_OUTPUT_DIRECTORY_KEY = "default_output_directory";

    /**
     * Enum constructor. Initializes the Preferences object and sets the default values if they don't exist.
     */
    Preferences() {
        prefs = java.util.prefs.Preferences.userNodeForPackage(Preferences.class);
        setDefaults(false);
    }

    /**
     * Sets the default values for the preferences. The language and currency are set to the default values for the default locale.
     * The default output directory is set to the current path.
     *
     * @param bypassCheck true if the check for existing preferences should be bypassed, false otherwise
     */
    private void setDefaults(final boolean bypassCheck) {
        try {
            if (bypassCheck || (prefs != null && prefs.keys().length == 0)) {
                Locale defaultLocale = Locale.getDefault();
                prefs.put(THEME_KEY, "light"); //unused for now
                prefs.put(LANGUAGE_KEY, defaultLocale.getLanguage());
                prefs.put(DEFAULT_CURRENCY_KEY, Currency.getInstance(defaultLocale).getCurrencyCode());
                prefs.put(DEFAULT_OUTPUT_DIRECTORY_KEY, Property.currentPath());
            }
        } catch (final BackingStoreException e) {
            logger.error("Failed to set default preferences", e);
        }
    }

    /**
     * Sets the theme for the application.
     * <br><br> Not implemented yet
     *
     * @param theme the theme to set. Valid values are "light" or "dark".
     * @return true if the theme was set successfully, false otherwise.
     */
    private boolean setTheme(final String theme) {
        if (theme == null || (!theme.equals("light") && !theme.equals("dark"))) {
            logger.error("Invalid theme: " + theme);
            return false;
        }
        prefs.put(THEME_KEY, theme);
        return true;
    }

    /**
     * Retrieves the theme preference from the user's preferences.
     *
     * @return the value of the theme preference, or "light" if not found
     */
    private String getTheme() {
        return prefs.get(THEME_KEY, "light");
    }

    /**
     * Sets the language preference for the application.
     *
     * @param language the language to set. Valid values are "en" or "es" or "gl".
     */
    public void setLanguage(final String language) {
        prefs.put(LANGUAGE_KEY, language);
        Locale.setDefault(new Locale(language));
    }

    /**
     * Retrieves the language preference of the user.
     *
     * @return The language code of the user's preferred language.
     */
    public String getLanguage() {
        return prefs.get(LANGUAGE_KEY, Locale.getDefault().getLanguage());
    }

    /**
     * Sets the default currency for the application. This is a convenience to make operations faster
     * for the user.
     *
     * @param currency the currency to set as the default
     * @return true if the default currency was set successfully, false otherwise
     */
    public boolean setDefaultCurrency(final Currency currency) {
        if (currency == null) {
            logger.error("Null currency!");
            return false;
        }
        prefs.put(DEFAULT_CURRENCY_KEY, currency.getCurrencyCode());
        return true;
    }

    /**
     * Retrieves the default currency stored in preferences or the default locale's currency.
     *
     * @return the default currency
     */
    public String getDefaultCurrency() {
        return prefs.get(DEFAULT_CURRENCY_KEY, Locale.getDefault().getLanguage());
    }

    /**
     * Sets the default output directory for exported files.
     *
     * @param  directory  the directory to set as the default output directory
     * @return            true if the directory is valid and the default output directory is set successfully, false otherwise
     */
    public boolean setDefaultOutputDirectory(final File directory) {
        if (directory == null || !directory.isDirectory()) {
            logger.error("Invalid output directory: " + directory);
            return false;
        }
        prefs.put(DEFAULT_OUTPUT_DIRECTORY_KEY, directory.getAbsolutePath());
        return true;
    }

    /**
     * Retrieves the default output directory for the application.
     *
     * @return  the default output directory path
     */
    public String getDefaultOutputDirectory() {
        return prefs.get(DEFAULT_OUTPUT_DIRECTORY_KEY, Property.currentPath());
    }

    /**
     * Sets all the preference values to their default state.
     *
     */
    public void defaultAll() {
        setDefaults(true);
    }

    /**
     * Clears all preferences from the registry.
     *
     * @return true if all preferences are cleared successfully, false otherwise
     */
    public boolean clearAll() {
        try {
            prefs.clear();
            return true;
        } catch (final BackingStoreException e) {
            logger.error("Failed to clear preferences", e);
            return false;
        }
    }
}
