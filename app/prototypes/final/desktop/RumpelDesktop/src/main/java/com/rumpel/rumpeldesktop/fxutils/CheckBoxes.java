package com.rumpel.rumpeldesktop.fxutils;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Map;

/**
 * Utility class for dealing with check boxes
 */
public final class CheckBoxes {
    /**
     * Private constructor to prevent instantiation
     */
    private CheckBoxes() {
        throw new AssertionError("Utility classes cannot be instantiated");
    }

    /**
     * Convenience method for forms with password fields and a checkbox to show or hide the password. It synchronizes the content
     * when hidden or shown and listens for the state of the checkbox such that when the checkbox is selected, the passwords are
     * shown, and are hidden otherwise.
     *
     * @param checkBox The checkbox to which to bind the functionalities.
     * @param fields   A map of fields consisting of pairs of password fields bound to text fields for password visibility.
     */
    public static void setUpForPasswords(final CheckBox checkBox, final Map<PasswordField, TextField> fields) {
        fields.forEach((passwordField, textField) -> passwordField.textProperty().bindBidirectional(textField.textProperty()));
        checkBox.setOnAction(event -> {
            if (checkBox.isSelected()) fields.forEach((passField, showField) -> {
                showField.setVisible(true);
                passField.setVisible(false);
            });
            else fields.forEach((passField, showField) -> {
                showField.setVisible(false);
                passField.setVisible(true);
            });
        });
    }

    /**
     * Binds the selected state of a CheckBox to multiple Nodes.
     *
     * @param checkBox the CheckBox whose state will be bound
     * @param inverted a boolean indicating whether the state should be inverted
     * @param nodes    the Nodes to bind the state to
     */
    public static void bindStateToNodes(final CheckBox checkBox, boolean inverted, final Node... nodes) {
        checkBox.setOnAction(event -> {
            if (inverted) {
                for (Node node : nodes) {
                    node.setDisable(!checkBox.isSelected());
                }
            } else {
                for (Node node : nodes) {
                    node.setDisable(checkBox.isSelected());
                }
            }
        });
    }
}
