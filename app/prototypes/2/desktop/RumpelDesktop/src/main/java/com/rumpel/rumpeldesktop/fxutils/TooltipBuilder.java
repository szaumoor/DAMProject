package com.rumpel.rumpeldesktop.fxutils;

import javafx.scene.control.Tooltip;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public final class TooltipBuilder {

    private final Tooltip tooltip = new Tooltip();

    public TooltipBuilder withText(final String name) {
        tooltip.setText(name);
        return this;
    }

    public TooltipBuilder withFontSize(final double size) {
        tooltip.setStyle(Styles.getFontSize(size));
        return this;
    }

    public TooltipBuilder withAlignment(final TextAlignment alignment) {
        tooltip.setTextAlignment(alignment);
        return this;
    }

    public TooltipBuilder withDuration(final Duration duration) {
        tooltip.setShowDuration(duration);
        return this;
    }

    public TooltipBuilder withDelay(final Duration duration) {
        tooltip.setShowDelay(duration);
        return this;
    }


    public Tooltip build() {
        return tooltip;
    }
}
