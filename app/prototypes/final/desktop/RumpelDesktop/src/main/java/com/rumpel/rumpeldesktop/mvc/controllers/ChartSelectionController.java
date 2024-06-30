package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.fxutils.Spinners;
import com.rumpel.rumpeldesktop.fxutils.prefs.Preferences;
import com.rumpel.rumpeldesktop.mvc.views.ChartsViewDialog;
import com.szaumoor.rumple.model.entities.types.reports.ReportType;
import com.szaumoor.rumple.utils.Money;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Currency;
import java.util.ResourceBundle;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;
import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.errorAndWait;
import static com.szaumoor.rumple.model.entities.types.reports.ReportType.*;

/**
 * Controller for the chart selection dialog and all the functions
 */
public final class ChartSelectionController implements Initializable {
    @FXML
    private ComboBox<Currency> currComboBox;
    @FXML
    private Label lblMonth;
    @FXML
    private Label lblYear;
    @FXML
    private BorderPane mainPane;
    @FXML
    private Spinner<Integer> spinnerMonth;
    @FXML
    private Spinner<Integer> spinnerYear;
    @FXML
    private ComboBox<UpdateData> typeComboBox;

    /**
     *  Record to store a report type along with the text to be displayed for convenience
     * @param report the report type
     * @param text the text to be displayed
     */
    record UpdateData(ReportType report, String text) {
        @Override
        public String toString() {
            return text;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Spinners.setForMonths(spinnerMonth, LocalDate.now().getMonthValue());
        Spinners.setForYears(spinnerYear);

        final var items = typeComboBox.getItems();
        items.add(new UpdateData(TAG_STATS_MONTH, "1. " + getString("report_tag_totals_month")));
        items.add(new UpdateData(TAG_STATS_YEAR, "2. " + getString("report_tag_totals_year")));
        items.add(new UpdateData(TAG_STATS_ACROSS_YEARS, "3. " + getString("report_tag_totals_across_years")));
        items.add(new UpdateData(PAYMENT_STATS_MONTH, "4. " + getString("report_pm_month")));
        items.add(new UpdateData(PAYMENT_STATS_YEAR, "5. " + getString("report_pm_year")));
        items.add(new UpdateData(PAYMENT_STATS_ACROSS_YEARS, "6. " + getString("report_pm_across_years")));
        items.add(new UpdateData(EXPENSES_YEAR, "7. " + getString("report_expenses_month")));
        items.add(new UpdateData(EXPENSES_YEARLY, "8. " + getString("report_expenses_year")));
        items.add(new UpdateData(EXPENSES_IN_MONTH, "9. " + getString("report_expenses_in_month")));

        currComboBox.setItems(FXCollections.observableArrayList(Money.currenciesSortedByCode()));
        currComboBox.setValue(Currency.getInstance(Preferences.instance.getDefaultCurrency()));
        typeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            var value = observable.getValue();
            if (value != null) {
                switch (value.report) {
                    case TAG_STATS_MONTH, PAYMENT_STATS_MONTH, EXPENSES_IN_MONTH -> {
                        spinnerMonth.setDisable(false);
                        lblMonth.setDisable(false);
                        spinnerYear.setDisable(false);
                        lblYear.setDisable(false);
                        lblYear.setText(getString("year"));
                    }
                    case EXPENSES_YEAR, EXPENSES_YEARLY, TAG_STATS_ACROSS_YEARS,
                            PAYMENT_STATS_ACROSS_YEARS, TAG_STATS_YEAR, PAYMENT_STATS_YEAR -> {
                        spinnerMonth.setDisable(true);
                        lblMonth.setDisable(true);
                        spinnerYear.setDisable(false);
                        lblYear.setDisable(false);
                        if (value.report == EXPENSES_YEARLY || value.report == TAG_STATS_ACROSS_YEARS ||
                                value.report == PAYMENT_STATS_ACROSS_YEARS)
                            lblYear.setText(getString("year_beginning"));
                        else lblYear.setText(getString("year"));
                    }
                    default -> {
                        spinnerMonth.setDisable(true);
                        lblMonth.setDisable(true);
                        spinnerYear.setDisable(true);
                        lblYear.setDisable(true);
                        lblYear.setText(getString("year"));
                    }
                }
            }
        });
    }

    @FXML
    private void openChartView() {
        if (currComboBox.getValue() == null) {
            errorAndWait(getString("no_currency_error"));
            return;
        }
        if (typeComboBox.getValue() == null) {
            errorAndWait(getString("no_report_type_error"));
            return;
        }
        new ChartsViewDialog(mainPane.getScene().getWindow(), currComboBox.getValue(), Year.of(spinnerYear.getValue()),
                Month.of(spinnerMonth.getValue()), typeComboBox.getValue().report).show();
    }
}
