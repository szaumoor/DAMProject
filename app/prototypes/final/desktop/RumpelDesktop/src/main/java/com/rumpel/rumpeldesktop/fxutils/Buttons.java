package com.rumpel.rumpeldesktop.fxutils;

import com.rumpel.rumpeldesktop.App;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * Utility class for tweaking buttons.
 */
public final class Buttons {
    /**
     * Private constructor to prevent instantiation by any means
     */
    private Buttons() {
        throw new AssertionError("Utility classes cannot be instantiated");
    }

    /**
     * Sets an icon button with the specified image on the given Button.
     *
     * @param  btn  the Button on which to set the icon
     * @param  img  the path to the image file
     * @throws NullPointerException if the Button or the image path is null
     */
    public static void setIconButton(final Button btn, final String img) {
        var imageAdd = new Image(App.class.getResource(img).toString());
        var value = new ImageView(imageAdd);
        value.setPreserveRatio(true);
        value.setFitHeight(btn.getPrefHeight());
        value.setFitWidth(btn.getPrefWidth());
        btn.setGraphic(value);
    }
}
