package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.fxutils.Spinners;
import com.szaumoor.rumple.db.utils.Pair;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;

import java.net.URL;
import java.time.Month;
import java.time.Year;
import java.util.ResourceBundle;

/**
 * Controller for the select month dialog
 */
public final class SelectMonthDialogController implements Initializable {
    @FXML
    private Spinner<Integer> year;
    @FXML
    private Spinner<Integer> month;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Spinners.setForNumbers(year, 1970, Year.now().getValue(), Year.now().getValue());
        Spinners.setForNumbers(month, 1, 12, 1);
    }

    public Pair<Year, Month> getMonth() {
        return new Pair<>(Year.of(year.getValue()), Month.of(month.getValue()));
    }

    public void setMonth(final Year year, final Month month) {
        this.year.getValueFactory().setValue(year.getValue());
        this.month.getValueFactory().setValue(month.getValue());
    }
}
