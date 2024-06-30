package com.rumpel.rumpeldesktop.mvc.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the About dialog
 */
public final class AboutDialogController implements Initializable {

    @FXML
    private Label lblAboutContact;
    @FXML
    private Label lblHeading;
    @FXML
    private Label lblAboutTxt;
    @FXML
    private Label lblPowered;
    private String content;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        content = lblHeading.getText() + "\n\n" + lblAboutTxt.getText() + lblAboutContact.getText() + "\n\n" + lblPowered.getText();
    }

    /**
     * Gets content from the dialog (used for clipboard)
     *
     * @return The content
     */
    public String getContent() {
        return content;
    }
}
