package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.db.DAOTag;
import com.rumpel.rumpeldesktop.fxutils.Styles;
import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.ItemBill;
import com.szaumoor.rumple.model.entities.Tag;
import com.szaumoor.rumple.utils.Money;
import com.szaumoor.rumple.utils.Numbers;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.input.KeyCode;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;
import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.errorAndWait;
import static javafx.collections.FXCollections.observableArrayList;

public class ItemBillDialogController implements Initializable {
    private record CheckedTag(SimpleBooleanProperty checkedProperty, SimpleObjectProperty<Tag> tagProperty) {

        public CheckedTag(boolean checked, Tag tag) {
            this(new SimpleBooleanProperty(), new SimpleObjectProperty<>());
            this.checkedProperty.set(checked);
            this.tagProperty.set(tag);
        }

        @Override
        public String toString() {
            return tagProperty.get().getName();
        }

    }

    @FXML
    private Label lblTotal;
    @FXML
    private TableColumn<ItemBill, String> nameCol;
    @FXML
    private TableColumn<ItemBill, String> priceCol;
    @FXML
    private TableView<ItemBill> tableView;
    @FXML
    private ListView<CheckedTag> tagList;
    @FXML
    private TextField nameField;

    @FXML
    private Spinner<Double> spinnerPrice;

    private Bill bill;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var tagDao = new DAOTag();
        spinnerPrice.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE));
        tagList.setItems(FXCollections.observableList(tagDao.getAll().stream().map(tag -> new CheckedTag(false, tag)).toList()));

        tagList.setCellFactory(CheckBoxListCell.forListView(CheckedTag::checkedProperty));

        tagList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        nameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        priceCol.setCellValueFactory(param -> new SimpleStringProperty(Money.formatCurrency(param.getValue().getPrice(), bill.getCurrency())));

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            final var value = observable.getValue();
            if (value != null) {
                tableView.requestFocus();
                nameField.setText(value.getName());
                spinnerPrice.getValueFactory().setValue(value.getPrice().doubleValue());
                for (var tag : tagList.getItems())
                    tag.checkedProperty().set(value.contains(tag.tagProperty().get()));
            }
        });

        tableView.setOnKeyPressed(event -> {
            var selectionModel = tableView.getSelectionModel();
            if (event.getCode() == KeyCode.DELETE && !selectionModel.getSelectedCells().isEmpty()) {
                tableView.getItems().remove(selectionModel.getSelectedItem());
                updateTotal();
            }
        });

        Styles.applyFontSize(16, tagList, tableView);
    }

    @FXML
    private void insertItem() {
        var name = nameField.getText();
        var price = BigDecimal.valueOf(spinnerPrice.getValue());
        var selectedTags = tagList.getItems().stream()
                .filter(checkedTag -> checkedTag.checkedProperty.get())
                .map(checkedTag -> checkedTag.tagProperty.get())
                .toList();
        var op = ItemBill.of(name, price, selectedTags);
        if (op.isEmpty()) {
            errorAndWait(getString("item_failed_creation"));
            return;
        }
        tableView.getItems().add(op.get());
        updateTotal();
    }

    @FXML
    private void modifyItem() {
        var item = tableView.getSelectionModel().getSelectedItem();
        if (item == null) {
            errorAndWait(getString("no_item_selected"));
            return;
        }
        var name = nameField.getText();
        var price = BigDecimal.valueOf(spinnerPrice.getValue());
        var selectedTags = tagList.getItems().stream()
                .filter(checkedTag -> checkedTag.checkedProperty.get())
                .map(checkedTag -> checkedTag.tagProperty.get())
                .toList();
        var nameSet = item.setName(name);
        if (!nameSet) {
            errorAndWait(getString("item_name_empty"));
            return;
        }
        var priceSet = item.setPrice(price);
        if (!priceSet) {
            errorAndWait(getString("item_price_not_in_range"));
            return;
        }
        item.clear();
        item.addAll(selectedTags);
        var items = tableView.getItems();
        items.set(items.indexOf(item), item);
    }

    private void updateTotal() {
        lblTotal.setText("Total: " + Money.formatCurrency(Numbers.sum(tableView.getItems()), bill.getCurrency()));
    }


    public List<ItemBill> getList() {
        return tableView.getItems();
    }

    public void setBill(final Bill bill) {
        this.bill = bill;
        if (bill.size() > 0) {
            tableView.setItems(observableArrayList(bill.stream().toList()));
            lblTotal.setText("Total: " + bill.format());
        } else tableView.setItems(observableArrayList());
    }

}
