package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.db.DAOBudget;
import com.rumpel.rumpeldesktop.db.DAOPaymentMethod;
import com.rumpel.rumpeldesktop.db.DAOUser;
import com.szaumoor.rumple.model.entities.Budget;
import com.szaumoor.rumple.model.entities.PaymentMethod;
import com.szaumoor.rumple.model.entities.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class BudgetDialogController implements Initializable {
    @FXML
    private ComboBox<Currency> currComboBox;
    @FXML
    private ComboBox<Budget> budgetsComboBox;
    @FXML
    private ComboBox<PaymentMethod> pmComboBox;

    @FXML
    private DatePicker endDatePicker;
    @FXML
    private DatePicker startDatePicker;

    @FXML
    private CheckBox checkPm;
    @FXML
    private CheckBox checkSoftLimit;

    @FXML
    private Spinner<Double> spinnerSoft;
    @FXML
    private Spinner<Double> spinnerHard;

    private DAOBudget dao;
    private DAOPaymentMethod daoPm;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final User user = DAOUser.getLoggedUser().get();
        dao = new DAOBudget(user);
        daoPm = new DAOPaymentMethod(user);

        spinnerSoft.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE));
        spinnerHard.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE));

        checkPm.setOnAction(event -> pmComboBox.setDisable(!checkPm.isSelected()));
        checkSoftLimit.setOnAction(event -> spinnerSoft.setDisable(!checkSoftLimit.isSelected()));

        LocalDate now = LocalDate.now();
        startDatePicker.setValue(now);
        endDatePicker.setValue(now.plusDays(7));

        var orderedCurrencies =
                Currency.getAvailableCurrencies()
                .stream()
                .sorted(Comparator.comparing(Currency::toString))
                .toList();
        currComboBox.setItems(FXCollections.observableList(orderedCurrencies));

        budgetsComboBox.setItems(FXCollections.observableArrayList(dao.getAll()));
        pmComboBox.setItems(FXCollections.observableArrayList(daoPm.getAll()));
    }

    @FXML
    public void insertBudget() {
        var startDate = startDatePicker.getValue().atStartOfDay(ZoneId.systemDefault());
        var endDate = endDatePicker.getValue().atStartOfDay(ZoneId.systemDefault());

        var softLimit = checkSoftLimit.isSelected() ? spinnerSoft.getValue() : null;
        var hardLimit = spinnerHard.getValue();

        var currency = currComboBox.getValue();
        var pm = pmComboBox.getValue();


    }
}
