package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.db.DAOPaymentMethod;
import com.rumpel.rumpeldesktop.db.DAOUser;
import com.rumpel.rumpeldesktop.mvc.views.utils.Alerts;
import com.szaumoor.rumple.model.entities.PaymentMethod;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;
import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.*;
import static com.szaumoor.rumple.db.utils.Outcome.SUCCESS;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

public class PaymentMethodController implements Initializable {
    @FXML
    private TextField newPmField;
    @FXML
    private ComboBox<PaymentMethod> pmComboBox;
    private DAOPaymentMethod dao;
    private ObservableList<PaymentMethod> pms;

    private static final Logger logger = LogManager.getLogger();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dao = new DAOPaymentMethod();
        pmComboBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<PaymentMethod> call(ListView<PaymentMethod> param) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(PaymentMethod item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else setText(item.getName());
                    }
                };
            }
        });
        pms = FXCollections.observableArrayList(dao.getAll());
        pmComboBox.setItems(pms);
        pmComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (observable.getValue() != null) newPmField.setText(observable.getValue().getName());
        });
        System.out.println(pmComboBox.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void insertPaymentMethod() {
        final var methodName = newPmField.getText();
        if (methodName.isBlank()) errorAndWait(getString("cannot_insert_empty_field"));
        else {
            final var op = dao.get(methodName);
            if (op.isPresent()) {
                errorAndWait(getString("pm_exists_already"));
            } else {
                var pm = PaymentMethod.of(methodName);
                if (pm.isPresent()) {
                    var extracted = pm.get();
                    extracted.setUser(DAOUser.getLoggedUser());
                    var insert = dao.insert(extracted);
                    if (insert == SUCCESS) {
                        pms.add(extracted);
                        infoAndWait(getString("pm_inserted"));
                    } else Alerts.showAndWait(INFORMATION, getString("connection_error"), getString("error"), "");
                } else
                    logger.error("Payment method could not be created somehow, re-check conditional validation of payment methods");
            }
        }
        newPmField.clear();
    }

    @FXML
    private void modifyPaymentMethod() {
        if (pmComboBox.getSelectionModel().getSelectedIndex() == -1) errorAndWait(getString("no_item_selected"));
        else {
            var selectedPm = pmComboBox.getSelectionModel().getSelectedItem();
            if (!selectedPm.setName(newPmField.getText())) errorAndWait(getString("pm_invalid_params"));
            else {
                var out = dao.modify(selectedPm);
                if (out == SUCCESS) {
                    infoAndWait(getString("pm_modified"));
                    pmComboBox.getSelectionModel().select(-1);
                    pmComboBox.getSelectionModel().select(selectedPm);
                } else logger.error("Some unknown error occurred...");
            }
        }
    }

    @FXML
    private void deletePaymentMethod() {
        if (pmComboBox.getSelectionModel().getSelectedIndex() == -1) errorAndWait(getString("no_item_selected"));
        else {
            var buttonType = confirmAndWait(getString("confirmation_delete_pm"));
            if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {
                var selectedPm = pmComboBox.getValue();
                var out = dao.delete(selectedPm);
                if (out == SUCCESS) {
                    pmComboBox.getItems().remove(selectedPm);
                    pmComboBox.getSelectionModel().select(-1);
                } else logger.error("Some unknown error occurred...");
            }
        }
    }
}
