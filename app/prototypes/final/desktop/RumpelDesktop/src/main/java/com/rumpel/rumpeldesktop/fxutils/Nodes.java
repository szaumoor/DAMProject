package com.rumpel.rumpeldesktop.fxutils;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Utility class for Nodes
 */
public final class Nodes {
    /**
     * Private constructor to prevent instantiation
     */
    private Nodes() {
        throw new AssertionError("Utility classes cannot be instantiated");
    }

    /**
     * Sets the given event handler for the onMouseClicked event
     * on the specified nodes.
     *
     * @param value the event handler to set
     * @param node  the nodes to set the event handler on
     */
    public static void setOnMouseClicked(final EventHandler<? super MouseEvent> value, final Node... node) {
        for (final Node n : node) n.setOnMouseClicked(value);
    }

    /**
     * Disables the specified nodes.
     *
     * @param node the nodes to disable
     */
    public static void disable(final Node... node) {
        for (final Node n : node) n.setDisable(true);
    }

    /**
     * Enables the given nodes by setting their disable property to false.
     *
     * @param node the nodes to enable
     */
    public static void enable(final Node... node) {
        for (final Node n : node) n.setDisable(false);
    }
}
