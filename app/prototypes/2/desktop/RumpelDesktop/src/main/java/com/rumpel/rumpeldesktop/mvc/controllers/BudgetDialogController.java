package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.db.DAOBudget;
import com.rumpel.rumpeldesktop.db.DAOPaymentMethod;
import com.rumpel.rumpeldesktop.db.DAOUser;
import com.rumpel.rumpeldesktop.fxutils.Styles;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.Budget;
import com.szaumoor.rumple.model.entities.PaymentMethod;
import com.szaumoor.rumple.model.entities.types.Interval;
import com.szaumoor.rumple.model.entities.types.Limit;
import com.szaumoor.rumple.utils.Money;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static com.rumpel.rumpeldesktop.fxutils.Dialogs.error;
import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;
import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.*;
import static com.szaumoor.rumple.db.utils.Outcome.SUCCESS;

public class BudgetDialogController implements Initializable {
    @FXML
    private Label lblPm;
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
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dao = new DAOBudget();
        final var daoPm = new DAOPaymentMethod();

        spinnerSoft.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE));
        spinnerHard.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE));

        checkPm.setOnAction(event -> pmComboBox.setDisable(!checkPm.isSelected()));
        checkSoftLimit.setOnAction(event -> {
            spinnerSoft.setDisable(!checkSoftLimit.isSelected());
            lblPm.setDisable(!checkSoftLimit.isSelected());
        });

        var now = LocalDate.now();
        startDatePicker.setValue(now);
        endDatePicker.setValue(now.plusDays(7));

        currComboBox.setItems(FXCollections.observableList(Money.currenciesSortedByCode()));
        currComboBox.setOnKeyPressed(event -> {
            if (event.getCode().isLetterKey()) {
                String aChar = event.getCode().getChar().toUpperCase();
                var first = currComboBox.getItems().stream().filter(c -> c.getCurrencyCode().startsWith(aChar)).findFirst();
                first.ifPresent(currency -> currComboBox.getSelectionModel().select(currency));
            }
        });

        budgetsComboBox.setItems(FXCollections.observableArrayList(dao.getAll()));
        pmComboBox.setItems(FXCollections.observableArrayList(daoPm.getAll()));

        budgetsComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            var value = observable.getValue();
            if (value != null) {
                startDatePicker.setValue(value.getInterval().startDate().toLocalDate());
                endDatePicker.setValue(value.getInterval().endDate().toLocalDate());
                currComboBox.getSelectionModel().select(value.getLimit().currency());

                double softLimit = value.getLimit().softLimit().doubleValue();
                spinnerSoft.getValueFactory().setValue(softLimit);
                checkSoftLimit.setSelected(softLimit > 0);
                spinnerSoft.setDisable(!(softLimit > 0));

                var pm = value.getPaymentMethod();
                spinnerHard.getValueFactory().setValue(value.getLimit().hardLimit().doubleValue());
                checkPm.setSelected(pm != null);
                checkPm.setSelected(pm != null);
                lblPm.setDisable(pm == null);
                pmComboBox.setDisable(pm == null);
                pmComboBox.getSelectionModel().select(pm);
            }
        });
        Styles.applyFontSize(15, budgetsComboBox, currComboBox, pmComboBox, spinnerHard, spinnerSoft, startDatePicker, endDatePicker);
    }

    private void resetFields() {
        var now = LocalDate.now();
        startDatePicker.setValue(now);
        endDatePicker.setValue(now.plusDays(7));

        spinnerSoft.getValueFactory().setValue(0.0);
        spinnerHard.getValueFactory().setValue(0.0);

        currComboBox.getSelectionModel().clearSelection();
        budgetsComboBox.getSelectionModel().select(0);
        pmComboBox.getSelectionModel().select(0);
    }

    @FXML
    public void insertBudget() {
        var startDate = startDatePicker.getValue().atStartOfDay(ZoneId.systemDefault());
        var endDate = endDatePicker.getValue().atStartOfDay(ZoneId.systemDefault());
        var softLimit = checkSoftLimit.isSelected() ? BigDecimal.valueOf(spinnerSoft.getValue()) : BigDecimal.ZERO;
        var hardLimit = BigDecimal.valueOf(spinnerHard.getValue());
        var currency = currComboBox.getValue();
        var pm = pmComboBox.getValue();

        var opInterval = Interval.of(startDate, endDate);
        if (error(opInterval, getString("invalid_budget_dates"))) return;
        var opLimit = Limit.of(softLimit, hardLimit, currency);
        if (error(opLimit, getString("invalid_budget_limits"))) return;
        var budget = Budget.of(opInterval.get(), opLimit.get(), pm, DAOUser.getLoggedUser());
        if (error(budget, getString("budget_failed_creation"))) return;

        final Outcome insert = dao.insert(budget.get());
        switch (insert) {
            case ERROR -> errorAndWait(getString("budget_failed_creation"));
            case UNIQUE_EXISTS -> errorAndWait(getString("budget_already_exists"));
            case SUCCESS -> {
                infoAndWait(getString("budget_inserted"));
                budgetsComboBox.getItems().add(budget.get());
                resetFields();
            }
        }
    }

    @FXML
    public void modifyBudget() {
        if (budgetsComboBox.getSelectionModel().getSelectedIndex() == -1) errorAndWait(getString("no_item_selected"));
        else {
            var budget = budgetsComboBox.getValue();

            var startDate = startDatePicker.getValue().atStartOfDay(ZoneId.systemDefault());
            var endDate = endDatePicker.getValue().atStartOfDay(ZoneId.systemDefault());
            var softLimit = BigDecimal.valueOf(spinnerSoft.getValue());
            var hardLimit = BigDecimal.valueOf(spinnerHard.getValue());
            var currency = currComboBox.getValue();

            var pm = pmComboBox.getValue();
            budget.setPaymentMethod(pm);

            var opInterval = Interval.of(startDate, endDate);
            if (opInterval.isEmpty()) {
                errorAndWait(getString("invalid_interval"));
                return;
            }
            budget.setInterval(opInterval.get());

            var opLimit = Limit.of(softLimit, hardLimit, currency);
            if (opLimit.isEmpty()) {
                errorAndWait(getString("invalid_budget_limits"));
                return;
            }
            budget.setLimit(opLimit.get());

            var modify = dao.modify(budget);
            if (modify == Outcome.SUCCESS) {
                budgetsComboBox.getSelectionModel().select(-1);
                budgetsComboBox.getSelectionModel().select(budget);
                infoAndWait(getString("budget_modified"));
            } else logger.error("Unknown error when modifying, check code or database for possible mistakes");
        }
    }

    @FXML
    public void deleteBudget() {
        if (budgetsComboBox.getSelectionModel().getSelectedIndex() == -1) errorAndWait(getString("no_item_selected"));
        else {
            var buttonType = confirmAndWait(getString("confirmation_delete_budget"));
            if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {
                var out = dao.delete(budgetsComboBox.getValue());
                if (out == SUCCESS) {
                    budgetsComboBox.getItems().remove(budgetsComboBox.getValue());
                    budgetsComboBox.getSelectionModel().select(-1);
                } else logger.error("Some unknown error occurred...");
            }
        }
    }
}
