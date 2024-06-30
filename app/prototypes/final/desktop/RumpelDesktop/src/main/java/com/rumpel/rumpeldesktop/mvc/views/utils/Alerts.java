package com.rumpel.rumpeldesktop.mvc.views.utils;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.util.Objects;
import java.util.Optional;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;

/**
 * Utility class to show alert dialogs easily
 */
public final class Alerts {
    private Alerts() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    /**
     * Builds an Alert with the specified parameters.
     *
     * @param alertType   the type of the alert
     * @param contentText the text to be displayed in the alert's content area
     * @param titleText   the text to be displayed in the alert's title area
     * @param headerText  the text to be displayed in the alert's header area
     * @return the built Alert object
     */
    public static Alert build(final Alert.AlertType alertType, final String contentText, final String titleText, final String headerText) {
        final Alert alert = new Alert(Objects.nonNull(alertType) ? alertType : Alert.AlertType.CONFIRMATION);
        DialogPane dialogPane = alert.getDialogPane();
        setStyle(dialogPane);
        if (contentText != null) alert.setContentText(contentText);
        if (titleText != null) alert.setTitle(titleText);
        if (headerText != null) alert.setHeaderText(headerText);
        return alert;
    }

    /**
     * Sets the style for the given DialogPane.
     *
     * @param dialogPane the DialogPane to set the style for
     */
    private static void setStyle(final DialogPane dialogPane) {
        dialogPane.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        ObservableList<String> styleClassPane = dialogPane.getStyleClass();
        styleClassPane.add("panel");
        styleClassPane.add("alert");
        styleClassPane.add("panel-danger");
        Button button = (Button) dialogPane.lookupButton(ButtonType.OK);
        ObservableList<String> styleClass = button.getStyleClass();
        styleClass.add("btn");
        styleClass.add("btn-danger");
    }

    /**
     * Shows an alert with the specified alert type, content text, title text, and header text.
     *
     * @param alertType   the type of the alert
     * @param contentText the text to be displayed in the alert's content area
     * @param titleText   the text to be displayed in the alert's title bar
     * @param headerText  the text to be displayed in the alert's header area
     */
    public static void show(final Alert.AlertType alertType, final String contentText, final String titleText, final String headerText) {
        build(alertType, contentText, titleText, headerText).show();
    }

    /**
     * Generates an alert with the specified alert type, content text, title text, and header text,
     * and displays it to the user. It then waits for the user to close the alert and returns the
     * button type that was clicked.
     *
     * @param alertType   the type of the alert (e.g., INFORMATION, WARNING, ERROR)
     * @param contentText the text to be displayed in the content area of the alert
     * @param titleText   the text to be displayed in the title bar of the alert
     * @param headerText  the text to be displayed in the header area of the alert
     * @return the button type that was clicked by the user
     */
    public static Optional<ButtonType> showAndWait(final Alert.AlertType alertType, final String contentText, final String titleText, final String headerText) {
        return build(alertType, contentText, titleText, headerText).showAndWait();
    }

    /**
     * Displays an error message with the given content.
     *
     * @param content the error message content
     */
    public static void error(final String content) {
        show(Alert.AlertType.ERROR, content, getString("error"), "");
    }

    /**
     * Displays an error message with the given content and header.
     *
     * @param content the content of the error message
     * @param header  the header of the error message
     */
    public static void error(final String content, final String header) {
        show(Alert.AlertType.ERROR, content, getString("error"), header);
    }

    /**
     * Displays a warning message with the given content.
     *
     * @param content the content of the warning message
     */
    public static void warning(final String content) {
        show(Alert.AlertType.WARNING, content, getString("warning"), "");
    }

    /**
     * Displays a warning alert with the specified content and header.
     *
     * @param content the content of the warning alert
     * @param header  the header of the warning alert
     */
    public static void warning(final String content, final String header) {
        show(Alert.AlertType.WARNING, content, getString("warning"), header);
    }

    /**
     * Generates an error alert with the given content and waits for a response.
     *
     * @param content the content of the error alert
     * @return an optional ButtonType representing the user's response
     */
    public static Optional<ButtonType> errorAndWait(final String content) {
        return showAndWait(Alert.AlertType.ERROR, content, getString("error"), "");
    }

    /**
     * Generates an error alert with the given content and header,
     * and waits for user interaction.
     *
     * @param content the content of the error alert
     * @param header  the header of the error alert
     * @return an Optional<ButtonType> representing the button
     * clicked by the user
     */
    public static Optional<ButtonType> errorAndWait(final String content, final String header) {
        return showAndWait(Alert.AlertType.ERROR, content, getString("error"), header);
    }

    /**
     * Generates a warning alert dialog with the given content and waits for a button press.
     *
     * @param content the content to display in the alert dialog
     * @return an Optional<ButtonType> representing the button that was pressed
     */
    public static Optional<ButtonType> warnAndWait(final String content) {
        return showAndWait(Alert.AlertType.WARNING, content, getString("error"), "");
    }

    /**
     * Generates a warning alert with the given content and header, and waits for user interaction.
     *
     * @param content the content of the warning alert
     * @param header  the header of the warning alert
     * @return the optional button type representing the user's interaction with the alert
     */
    public static Optional<ButtonType> warnAndWait(final String content, final String header) {
        return showAndWait(Alert.AlertType.WARNING, content, getString("error"), header);
    }

    /**
     * A method that displays an informational message.
     *
     * @param content the content of the message
     */
    public static void info(final String content) {
        show(Alert.AlertType.INFORMATION, content, getString("info"), "");
    }

    /**
     * Displays an information alert with the given content and header.
     *
     * @param content the content to be displayed in the alert
     * @param header  the header of the alert
     */
    public static void info(final String content, final String header) {
        show(Alert.AlertType.INFORMATION, content, getString("info"), header);
    }

    /**
     * Generates an informational alert with the given content, and waits for user interaction.
     *
     * @param content the content to display in the alert
     * @return an optional ButtonType representing the user's choice
     */
    public static Optional<ButtonType> infoAndWait(final String content) {
        return showAndWait(Alert.AlertType.INFORMATION, content, getString("info"), "");
    }

    /**
     * Generates an informational alert with the given content and header, and waits for user interaction.
     *
     * @param content the content of the informational alert
     * @param header  the header of the informational alert
     * @return the optional button type representing the user's interaction with the alert
     */
    public static Optional<ButtonType> infoAndWait(final String content, final String header) {
        return showAndWait(Alert.AlertType.INFORMATION, content, getString("info"), header);
    }

    /**
     * Generates a confirmation alert with the given content and header, and waits for user interaction.
     *
     * @param content the content of the confirmation alert
     */
    public static void confirm(final String content) {
        show(Alert.AlertType.CONFIRMATION, content, getString("success"), "");
    }

    /**
     * Shows a confirmation dialog with the specified content and header.
     *
     * @param content the content of the confirmation dialog
     * @param header  the header of the confirmation dialog
     */
    public static void confirm(final String content, final String header) {
        show(Alert.AlertType.CONFIRMATION, content, getString("success"), header);
    }

    /**
     * Generates a confirmation dialog with the specified content and waits for the user
     * to respond.
     *
     * @param content the content to display in the confirmation dialog
     * @return an Optional containing the ButtonType chosen by the user
     */
    public static Optional<ButtonType> confirmAndWait(final String content) {
        return showAndWait(Alert.AlertType.CONFIRMATION, content, getString("success"), "");
    }

    /**
     * Generates a confirmation dialog with the specified content and waits for the user
     * to respond.
     *
     * @param content the content to display in the confirmation dialog
     * @param header  the header of the confirmation dialog
     * @return an Optional containing the ButtonType chosen by the user
     */
    public static Optional<ButtonType> confirmAndWait(final String content, final String header) {
        return showAndWait(Alert.AlertType.CONFIRMATION, content, getString("success"), header);
    }
}
