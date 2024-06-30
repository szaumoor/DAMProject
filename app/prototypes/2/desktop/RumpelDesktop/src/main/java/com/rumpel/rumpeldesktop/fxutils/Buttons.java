package com.rumpel.rumpeldesktop.fxutils;

import com.rumpel.rumpeldesktop.App;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public enum Buttons {
    ;
    public static void setIconButton(final Button btn, final String img) {
        var imageAdd = new Image(App.class.getResource(img).toString());
        var value = new ImageView(imageAdd);
        value.setPreserveRatio(true);
        value.setFitHeight(btn.getPrefHeight());
        value.setFitWidth(btn.getPrefWidth());
        btn.setGraphic(value);
    }
}
