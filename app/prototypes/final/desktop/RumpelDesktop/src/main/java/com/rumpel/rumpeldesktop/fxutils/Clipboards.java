package com.rumpel.rumpeldesktop.fxutils;

import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;

import java.util.Map;

/**
 * Utility class to copy text to clipboard. Currently only handles plain text copies.
 */
public final class Clipboards {

    private static final Clipboard clipboard = Clipboard.getSystemClipboard();

    /**
     * Private constructor to prevent instantiation.
     */
    private Clipboards() {
        throw new AssertionError("Utility classes cannot be instantiated");
    }

    /**
     * Copies the given text to the clipboard.
     *
     * @param text Text to copy
     */
    public static void copy(final String text) {
        clipboard.setContent(Map.of(DataFormat.PLAIN_TEXT, text));
    }
}
