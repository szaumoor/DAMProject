package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.db.DAOPaymentMethod;
import com.rumpel.rumpeldesktop.db.DAOUser;
import com.rumpel.rumpeldesktop.db.utils.Outcome;
import com.rumpel.rumpeldesktop.mvc.views.utils.Alerts;
import com.szaumoor.rumple.model.entities.PaymentMethod;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.rumpel.rumpeldesktop.db.utils.Outcome.SUCCESS;
import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;
import static javafx.scene.control.Alert.AlertType.ERROR;
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
        dao = new DAOPaymentMethod(HomeController.user);
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
    }

    @FXML
    public void insertPaymentMethod() {
        final var methodName = newPmField.getText();
        if (methodName.isBlank()) {
            Alerts.showAndWait(ERROR, getString("cannot_insert_empty_field"), getString("error"), "");
            return;
        }
        final Optional<PaymentMethod> op = dao.get(methodName);
        if (op.isPresent()) {
            Alerts.showAndWait(ERROR, getString("pm_exists_already"), getString("error"), "");
        } else {
            Optional<PaymentMethod> pm = PaymentMethod.of(methodName);
            if (pm.isPresent()) {
                PaymentMethod extracted = pm.get();
                extracted.setUser(DAOUser.getLoggedUser().get());
                Outcome insert = dao.insert(extracted);
                if (insert == SUCCESS) {
                    pms.add(extracted);
                    Alerts.showAndWait(INFORMATION, getString("pm_inserted"), null, "");
                }
                else Alerts.showAndWait(INFORMATION, getString("connection_error"), getString("error"), "");
            }
            else logger.error("Payment method could not be created somehow, re-check conditional validation of payment methods");
        }
        newPmField.clear();
    }
}
