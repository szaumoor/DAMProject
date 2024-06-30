package com.rumpel.rumpeldesktop.mvc.views.utils;

import com.rumpel.rumpeldesktop.fxutils.Styles;
import javafx.scene.control.Tooltip;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * Builder class to build a {@link javafx.scene.control.Tooltip} easily.
 */
public final class TooltipBuilder {
    private final Tooltip tooltip = new Tooltip();

    /**
     * Constructs a tooltip with the given text.
     * @param text the text of the tooltip
     */
    public TooltipBuilder(final String text) {
        if (text == null) throw new IllegalArgumentException("text cannot be null");
        tooltip.setText(text);
    }

    /**
     * Sets the font size of the tooltip.
     *
     * @param  size  the font size to be set
     * @return       the TooltipBuilder object with the font size set
     */
    public TooltipBuilder withFontSize(final double size) {
        tooltip.setStyle(Styles.getFontSize(size));
        return this;
    }

    /**
     * Sets the alignment of the tooltip text.
     *
     * @param  alignment  the alignment to set
     * @return            the TooltipBuilder instance
     */
    public TooltipBuilder withAlignment(final TextAlignment alignment) {
        tooltip.setTextAlignment(alignment);
        return this;
    }
    /**
     * Sets the duration for which the tooltip is shown.
     *
     * @param  duration  the duration for the tooltip to be shown
     * @return           the TooltipBuilder instance
     */
    public TooltipBuilder withDuration(final Duration duration) {
        tooltip.setShowDuration(duration);
        return this;
    }
    /**
     * Sets the delay for showing the tooltip.
     *
     * @param  duration the duration of the delay
     * @return          the TooltipBuilder instance
     */
    public TooltipBuilder withDelay(final Duration duration) {
        tooltip.setShowDelay(duration);
        return this;
    }

    /**
     * Returns the built tooltip.
     *
     * @return  the Tooltip object that is built
     */
    public Tooltip build() {
        return tooltip;
    }
}
