package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.db.DAOBill;
import com.rumpel.rumpeldesktop.db.DAOPaymentMethod;
import com.rumpel.rumpeldesktop.db.DAOUser;
import com.rumpel.rumpeldesktop.fxutils.Dialogs;
import com.rumpel.rumpeldesktop.fxutils.Styles;
import com.rumpel.rumpeldesktop.mvc.controllers.interfaces.ItemInjector;
import com.rumpel.rumpeldesktop.mvc.views.ItemBillDialog;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.ItemBill;
import com.szaumoor.rumple.model.entities.PaymentMethod;
import com.szaumoor.rumple.utils.Money;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.List;
import java.util.ResourceBundle;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;
import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.*;

public class BillFormController implements Initializable, ItemInjector {
    public Button insertBtn;
    @FXML
    private Label lblTotal;
    @FXML
    private DatePicker billDatePicker;
    @FXML
    private ComboBox<Currency> currComboBox;
    @FXML
    private ComboBox<PaymentMethod> pmComboBox;
    @FXML
    private DialogPane mainPane;

    private Bill bill;

    private DAOBill daoBill;

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var dao = new DAOPaymentMethod();
        daoBill = new DAOBill();
        currComboBox.setItems(FXCollections.observableArrayList(Money.currenciesSortedByCode()));
        currComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> bill.setCurrency(currComboBox.getValue()));
        pmComboBox.setItems(FXCollections.observableArrayList(dao.getAll()));
        Styles.applyFontSize(15, pmComboBox, currComboBox);
    }

    @FXML
    private void openItemBillDialog() {
        var currencySelected = currComboBox.getSelectionModel().getSelectedItem() != null;
        if (Dialogs.error(!currencySelected, getString("currency_missing"))) return;
        var itemBillDialog = new ItemBillDialog(mainPane.getScene().getWindow(), this, bill);
        itemBillDialog.show();
    }


    @FXML
    private void modifyBill() {
        var modify = daoBill.modify(bill);
        if (modify == Outcome.SUCCESS) infoAndWait(getString("bill_modified"));
        else logger.error("bill couldn't be modified somehow");
    }

    @FXML
    private void insertBill() {
        if (Dialogs.error(bill.size() == 0, getString("bill_no_elements"))) {
            bill = new Bill();
            return;
        }
        var date = ZonedDateTime.of(billDatePicker.getValue(), LocalTime.now(), ZoneId.systemDefault());
        if (Dialogs.error(!bill.setDate(date), getString("bill_invalid_date"))){
            bill = new Bill();
            return;
        }
        var pm = pmComboBox.getValue();
        if (Dialogs.error(!bill.setPaymentMethod(pm), getString("bill_invalid_pm"))) {
            bill = new Bill();
            return;
        }
        var currency = currComboBox.getValue();
        if (Dialogs.error(!bill.setCurrency(currency), getString("bill_invalid_currency"))) {
            bill = new Bill();
            return;
        }

        bill.setUser(DAOUser.getLoggedUser());
        var insert = daoBill.insert(bill);
        switch (insert) {
            case ERROR -> errorAndWait(getString("bill_failed_creation"));
            case UNIQUE_EXISTS -> errorAndWait(getString("bill_already_exists"));
            case SUCCESS -> infoAndWait(getString("bill_inserted"));
        }
    }

    @Override
    public void insertItems(final List<ItemBill> items) {
        bill.clear();
        items.forEach(itemBill -> itemBill.setBill(bill));
        bill.addAll(items);
        bill.calcTotal();
        lblTotal.setText("Total: " + bill.format());
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(final Bill bill) {
        if (bill == null) {
            this.bill = new Bill();
            billDatePicker.setValue(LocalDate.now());
            insertBtn.setOnAction(event -> insertBill());
        } else {
            this.bill = bill;
            billDatePicker.setValue(bill.getDate().toLocalDate());
            insertBtn.setText(getString("bill_modify"));
            insertBtn.setOnAction(event -> modifyBill());
            lblTotal.setText("Total: " + bill.format());
            currComboBox.getSelectionModel().select(bill.getCurrency());
            pmComboBox.getSelectionModel().select(bill.getPaymentMethod());
        }
    }
}
