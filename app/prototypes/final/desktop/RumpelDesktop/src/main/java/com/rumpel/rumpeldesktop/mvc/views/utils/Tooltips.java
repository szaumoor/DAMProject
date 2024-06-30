package com.rumpel.rumpeldesktop.mvc.views.utils;

import javafx.scene.control.Tooltip;
import javafx.util.Duration;

/**
 * Utility class for setting up Tooltips in FX
 */
public final class Tooltips {
    private static final double defaultSize = 16.0;
    private static final Duration defaultDuration = Duration.seconds(0.8);

    /**
     * Private constructor to prevent instantiation
     */
    private Tooltips() {
        throw new AssertionError("Utility classes cannot be instantiated");
    }

    /**
     * Generates a default tooltip with the given text. The default has a size of 16.0 and a delay of 0.8 seconds.
     *
     * @param text the text of the tooltip
     * @return the generated tooltip
     * @throws IllegalArgumentException if the text is null
     */
    public static Tooltip getDefaultTooltip(final String text) {
        return new TooltipBuilder(text)
                .withFontSize(defaultSize)
                .withDelay(defaultDuration)
                .build();
    }
}
