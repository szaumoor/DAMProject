package com.rumpel.rumpeldesktop.mvc.views.utils;


public record Dimension(Width width, Height height) {
    public record Width(double value) {
    }

    public record Height(double value) {
    }
}
