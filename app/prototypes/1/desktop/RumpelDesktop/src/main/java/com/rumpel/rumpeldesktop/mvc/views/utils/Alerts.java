package com.rumpel.rumpeldesktop.mvc.views.utils;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.util.Objects;

public enum Alerts {
    ;

    public static Alert build(final Alert.AlertType alertType, final String contentText, final String titleText, final String headerText) {
        final Alert alert = new Alert(Objects.nonNull(alertType) ? alertType : Alert.AlertType.CONFIRMATION);
        DialogPane dialogPane = alert.getDialogPane();
        setStyle(dialogPane);
        if (contentText != null) alert.setContentText(contentText);
        if (titleText != null) alert.setTitle(titleText);
        if (headerText != null) alert.setHeaderText(headerText);
        return alert;
    }

    private static void setStyle(DialogPane dialogPane) {
        dialogPane.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        ObservableList<String> styleClassPane = dialogPane.getStyleClass();
        styleClassPane.add("panel");
        styleClassPane.add("panel-danger");
        Button button = (Button) dialogPane.lookupButton(ButtonType.OK);
        ObservableList<String> styleClass = button.getStyleClass();
        styleClass.add("btn");
        styleClass.add("btn-danger");
    }

    public static void show(final Alert.AlertType alertType, final String contentText, final String titleText, final String headerText) {
        build(alertType, contentText, titleText, headerText).show();
    }

    public static void showAndWait(final Alert.AlertType alertType, final String contentText, final String titleText, final String headerText) {
        build(alertType, contentText, titleText, headerText).showAndWait();
    }
}
