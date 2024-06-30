package com.rumpel.rumpeldesktop.mvc.views.utils;

/**
 * Record for dimensions of windows.
 *
 * @param width The width of the window.
 * @param height The height of the window.
 */
public record Dimension(Width width, Height height) {
    public record Width(double value) {
    }

    public record Height(double value) {
    }
}
