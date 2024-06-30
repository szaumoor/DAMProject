package com.rumpel.rumpeldesktop.fxutils;

import com.rumpel.rumpeldesktop.mvc.views.utils.Dimension;

/**
 * Class that contains all the dimensions used in the application to use when switching the window views.
 */
public final class Dimensions {
    private Dimensions() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    /**
     * The dimensions used for the login and signup views
     */
    public static final Dimension CREDENTIALS_DIMENSIONS = new Dimension(
            new Dimension.Width(750),
            new Dimension.Height(500)
    );

    /**
     * The dimensions used for the home view
     */
    public static final Dimension HOME_DIMENSIONS = new Dimension(
            new Dimension.Width(1024), new Dimension.Height(800)
    );

}
