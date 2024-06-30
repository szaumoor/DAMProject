package com.rumpel.rumpeldesktop.fxutils;

import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public enum Tooltips {
    ;

    private static final double defaultSize = 16.0;
    private static final Duration defaultDuration = Duration.seconds(0.8);

    public static Tooltip getDefaultTooltip(final String text) {
        return new TooltipBuilder()
                .withFontSize(defaultSize)
                .withText(text)
                .withDelay(defaultDuration)
                .build();
    }
}
