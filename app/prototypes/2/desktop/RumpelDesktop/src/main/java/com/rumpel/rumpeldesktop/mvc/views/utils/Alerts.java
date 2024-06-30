package com.rumpel.rumpeldesktop.mvc.views.utils;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.util.Objects;
import java.util.Optional;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;

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

    private static void setStyle(final DialogPane dialogPane) {
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

    public static Optional<ButtonType> showAndWait(final Alert.AlertType alertType, final String contentText, final String titleText, final String headerText) {
        return build(alertType, contentText, titleText, headerText).showAndWait();
    }

    public static void error(final String content) {
        show(Alert.AlertType.ERROR, content, getString("error"), "");
    }

    public static void error(final String content, final String header) {
        show(Alert.AlertType.ERROR, content, getString("error"), header);
    }

    public static Optional<ButtonType> errorAndWait(final String content) {
        return showAndWait(Alert.AlertType.ERROR, content, getString("error"), "");
    }

    public static Optional<ButtonType> errorAndWait(final String content, final String header) {
        return showAndWait(Alert.AlertType.ERROR, content, getString("error"), header);
    }

    public static void info(final String content) {
        show(Alert.AlertType.INFORMATION, content, getString("info"), "");
    }

    public static void info(final String content, final String header) {
        show(Alert.AlertType.INFORMATION, content, getString("info"), header);
    }

    public static Optional<ButtonType> infoAndWait(final String content) {
        return showAndWait(Alert.AlertType.INFORMATION, content, getString("info"), "");
    }

    public static Optional<ButtonType> infoAndWait(final String content, final String header) {
        return showAndWait(Alert.AlertType.INFORMATION, content, getString("info"), header);
    }

    public static void confirm(final String content) {
        show(Alert.AlertType.CONFIRMATION, content, getString("success"), "");
    }

    public static void confirm(final String content, final String header) {
        show(Alert.AlertType.CONFIRMATION, content, getString("success"), header);
    }

    public static Optional<ButtonType> confirmAndWait(final String content) {
        return showAndWait(Alert.AlertType.CONFIRMATION, content, getString("success"), "");
    }

    public static Optional<ButtonType> confirmAndWait(final String content, final String header) {
        return showAndWait(Alert.AlertType.CONFIRMATION, content, getString("success"), header);
    }
}
