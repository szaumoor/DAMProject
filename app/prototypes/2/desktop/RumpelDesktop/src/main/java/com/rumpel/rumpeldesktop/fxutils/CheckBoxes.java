package com.rumpel.rumpeldesktop.fxutils;

import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Map;

public enum CheckBoxes {
    ;

    /**
     * Convenience method for forms with password fields and a checkbox to show or hide the password. It synchronizes the content
     * when hidden or shown and listens for the state of the checkbox such that when the checkbox is selected, the passwords are
     * shown, and are hidden otherwise.
     *
     * @param checkBox The checkbox to which to bind the functionalities.
     * @param fields A map of fields consisting of pairs of password fields bound to text fields for password visibility.
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
}
