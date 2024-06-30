package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.db.DAOBill;
import com.rumpel.rumpeldesktop.db.DAOPaymentMethod;
import com.rumpel.rumpeldesktop.db.DAOUser;
import com.rumpel.rumpeldesktop.fxutils.Styles;
import com.rumpel.rumpeldesktop.fxutils.prefs.Preferences;
import com.rumpel.rumpeldesktop.mvc.controllers.injectors.ItemInjector;
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
import java.time.*;
import java.util.Currency;
import java.util.List;
import java.util.ResourceBundle;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;
import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.*;

/**
 * Controller for the bill dialog and all the functions
 */
public final class BillFormController implements Initializable, ItemInjector {
    @FXML
    private Label lblDate;
    @FXML
    private Button insertBtn;
    @FXML
    private Button insertItemsBtn;
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
        currComboBox.setValue(Currency.getInstance(Preferences.instance.getDefaultCurrency()));
        currComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> bill.setCurrency(currComboBox.getValue()));
        pmComboBox.setItems(FXCollections.observableArrayList(dao.getAll()));
        Styles.applyFontSize(15, pmComboBox, currComboBox);
    }

    @FXML
    private void openItemBillDialog() {
        var currencySelected = currComboBox.getSelectionModel().getSelectedItem() != null;
        if (!currencySelected) {
            errorAndWait(getString("currency_missing"));
            return;
        }
        var itemBillDialog = new ItemBillDialog(mainPane.getScene().getWindow(), this, bill);
        itemBillDialog.show();
    }


    @FXML
    private void modifyBill() {
       // bill.setDate(ZonedDateTime.of(billDatePicker.getValue(), LocalTime.now(), ZoneId.systemDefault())); // date should not be reset to avoid misleading information
        bill.setPaymentMethod(pmComboBox.getValue());
        bill.setCurrency(currComboBox.getValue());
        var modify = daoBill.modify(bill);
        if (modify == Outcome.SUCCESS) infoAndWait(getString("bill_modified"));
        else logger.error("bill couldn't be modified somehow");
    }

    @FXML
    private void insertBill() {
        if (bill.size() == 0) {
            errorAndWait(getString("bill_no_elements"));
            return;
        }
        var date = ZonedDateTime.of(billDatePicker.getValue(), LocalTime.now(), ZoneId.systemDefault());
        if (!bill.setDate(date)) {
            errorAndWait(getString("bill_invalid_date"));
            return;
        }
        var pm = pmComboBox.getValue();
        if (!bill.setPaymentMethod(pm)) {
            errorAndWait(getString("bill_invalid_pm"));
            return;
        }
        var currency = currComboBox.getValue();
        if (!bill.setCurrency(currency)) {
            errorAndWait(getString("bill_invalid_currency"));
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
            this.bill.setCurrency(currComboBox.getValue());
            billDatePicker.setValue(LocalDate.now());
            insertBtn.setOnAction(event -> insertBill());
        } else {
            this.bill = bill;
            billDatePicker.setValue(bill.getDate().toLocalDate());
            billDatePicker.setDisable(true);
            lblDate.setDisable(true);
            insertBtn.setText(getString("bill_modify"));
            insertItemsBtn.setText(getString("bill_modify_items"));
            insertBtn.setOnAction(event -> modifyBill());
            lblTotal.setText("Total: " + bill.format());
            currComboBox.getSelectionModel().select(bill.getCurrency());
            pmComboBox.getSelectionModel().select(bill.getPaymentMethod());
        }
    }

}
