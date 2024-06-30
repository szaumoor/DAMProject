package com.rumpel.rumpeldesktop.fxutils;

import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;

import java.util.Map;

public enum Clipboards {
    ;

    private static final Clipboard clipboard = Clipboard.getSystemClipboard();

    public static void copy(final String text) {
        clipboard.setContent(Map.of(DataFormat.PLAIN_TEXT, text));
    }
}
