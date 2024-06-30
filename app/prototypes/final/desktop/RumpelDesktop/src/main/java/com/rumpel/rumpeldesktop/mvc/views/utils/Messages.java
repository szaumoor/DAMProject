package com.rumpel.rumpeldesktop.mvc.views.utils;

import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * Utility class for displaying messages
 */
public final class Messages {
    /**
     * Private constructor to prevent instantiation
     */
    private Messages() {
        throw new AssertionError("Utility classes cannot be instantiated");
    }

    /**
     * Gets the base Notifications object, used for displaying short messages
     *
     * @return the Notifications object for displaying short messages
     */
    private static Notifications getBase() {
        return Notifications.create().hideAfter(Duration.seconds(5));
    }

    /**
     * Gets the base Notifications object, used for displaying long messages
     *
     * @return the Notifications object for displaying long messages
     */
    private static Notifications getBaseLong() {
        return Notifications.create().hideAfter(Duration.seconds(10));
    }

    /**
     * Shows a window with the given text as its content.
     *
     * @param owner The owner window of the dialog.
     * @param text  The text to be displayed in the dialog.
     */
    public static void show(final Window owner, final String text) {
        getBase().text(text).owner(owner).show();
    }

    /**
     * Shows a window with the given text as its content.
     *
     * @param owner The owner window of the dialog.
     * @param text  The text to be displayed in the dialog.
     */
    public static void showDanger(final Window owner, final String text) {
        getBase().text(text).owner(owner).showError();
    }

    /**
     * Shows information in a window.
     *
     * @param owner the owner window
     * @param text  the information text
     */
    public static void showInfo(final Window owner, final String text) {
        getBase().text(text).owner(owner).showInformation();
    }

    /**
     * Show a long text in a window.
     *
     * @param owner the owner window of the text
     * @param text  the long text to be shown
     */
    public static void showLong(final Window owner, final String text) {
        getBaseLong().text(text).owner(owner).show();
    }

    /**
     * Shows a warning message on the specified window.
     *
     * @param owner the window on which the warning is displayed
     * @param text  the text of the warning message
     */
    public static void showWarning(final Window owner, final String text) {
        getBase().text(text).owner(owner).showWarning();
    }

    /**
     * Shows a long danger message in a window.
     *
     * @param owner the window where the message should be shown
     * @param text  the text of the danger message
     */
    public static void showLongDanger(final Window owner, final String text) {
        getBaseLong().text(text).owner(owner).showError();
    }

    /**
     * Shows a long information message in a window.
     *
     * @param owner The window in which the information message should be displayed.
     * @param text  The text of the information message.
     */
    public static void showLongInfo(final Window owner, final String text) {
        getBaseLong().text(text).owner(owner).showInformation();
    }

    /**
     * Shows a long warning message on the specified window.
     *
     * @param owner the window on which the warning message should be displayed
     * @param text  the text of the warning message
     */
    public static void showLongWarning(final Window owner, final String text) {
        getBaseLong().text(text).owner(owner).showWarning();
    }
}
