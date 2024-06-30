package com.rumpel.rumpeldesktop.fxutils;

import javafx.scene.Node;
import javafx.scene.control.PopupControl;

public enum Styles {
    ;

    public static String getFontSize(final double fontSize) {
        return "-fx-font-size: " + fontSize;
    }

    public static void applyFontSize(final double fontSize, Node ... nodes) {
        for (var node : nodes) {
            node.setStyle(node.getStyle() + "; " + getFontSize(fontSize));
        }
    }

    public static void applyFontSize(final double fontSize, PopupControl... nodes) {
        for (var node : nodes) {
            node.setStyle(node.getStyle() + "; " + getFontSize(fontSize));
        }
    }
}
