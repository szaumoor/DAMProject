package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.mvc.views.ItemBillDialog;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DialogPane;

import java.net.URL;
import java.util.ResourceBundle;

public class BillFormController implements Initializable {

    @FXML
    private DialogPane mainPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void openItemBillDialog() {
        new ItemBillDialog(mainPane.getScene().getWindow()).show();
    }
}
