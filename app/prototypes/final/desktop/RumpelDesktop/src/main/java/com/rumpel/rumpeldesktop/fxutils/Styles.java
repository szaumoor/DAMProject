package com.rumpel.rumpeldesktop.fxutils;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.PopupControl;
import javafx.scene.text.FontWeight;

/**
 * Utility class
 */
public final class Styles {
    /**
     * Private constructor to prevent instantiation
     */
    private Styles() {
        throw new AssertionError("Utility classes cannot be instantiated");
    }

    /**
     * Returns the font size CSS property for a given font size.
     *
     * @param fontSize the font size to be converted to a CSS property
     * @return the CSS property representing the font size
     */
    public static String getFontSize(final double fontSize) {
        return "-fx-font-size: " + fontSize;
    }

    /**
     * Applies the specified font size to the given nodes.
     *
     * @param fontSize the font size to be applied
     * @param nodes    the nodes to apply the font size to
     */
    public static void applyFontSize(final double fontSize, Node... nodes) {
        for (var node : nodes) {
            var style = new SimpleStringProperty(node.getStyle() + "; " + getFontSize(fontSize));
            node.styleProperty().bind(Bindings.when(node.hoverProperty()).then(style).otherwise(style));
        }
    }

    /**
     * Applies the specified font size and font weight to the given nodes.
     *
     * @param fontSize   the font size to apply
     * @param fontWeight the font weight to apply
     * @param nodes      the nodes to apply the font size and font weight to
     */
    public static void applyFontSize(final double fontSize, final FontWeight fontWeight, Node... nodes) {
        for (var node : nodes) {
            var style = new SimpleStringProperty(node.getStyle() + "; " + getFontSize(fontSize) + "; -fx-font-weight: " + fontWeight.toString());
            node.styleProperty().bind(Bindings.when(node.hoverProperty()).then(style).otherwise(style));
        }
    }

    /**
     * Apply a font size to the given PopupControl nodes.
     *
     * @param fontSize the font size to be applied
     * @param nodes    the PopupControl nodes to apply the font size to
     */
    public static void applyFontSize(final double fontSize, PopupControl... nodes) {
        for (var node : nodes) {
            node.setStyle(node.getStyle() + "; " + getFontSize(fontSize));
        }
    }
}
