package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.mvc.views.ItemBillDialog;
import com.szaumoor.rumple.model.entities.Tag;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ItemBillDialogController implements Initializable {

    @FXML
    private BorderPane mainPane;
    @FXML
    private ListView<Tag> tagList;
    @FXML
    private TextField nameField;
    @FXML
    private Button insertBtn;
    @FXML
    private Spinner<Double> spinnerPrice;

    private final Map<Tag,Boolean> selected = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        spinnerPrice.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE));
        tagList.setItems(FXCollections.observableList(List.of(
                new Tag("Food"),
                new Tag("Vices"),
                new Tag("Drink"),
                new Tag("Fish"),
                new Tag("Energy expenses")
        )));
        tagList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Tag> call(ListView<Tag> param) {
                CheckBoxListCell<Tag> checkBoxListCell = new CheckBoxListCell<>() {
                    @Override
                    public void updateItem(Tag item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else setText(item.getName());
                    }
                };
                checkBoxListCell.setSelectedStateCallback(param1 -> {
                    SimpleBooleanProperty bool = new SimpleBooleanProperty();
                    bool.addListener((observable, oldValue, newValue) -> selected.put(param1, newValue));
                    return bool;
                });
                return checkBoxListCell;
            }
        });

        tagList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        loadTagMappings();
    }

    private void loadTagMappings() { // return tag of the user and map it in the list view by default false (unselected)
    }

    @FXML
    private void openItemBillDialog() {
        new ItemBillDialog(mainPane.getScene().getWindow()).show();
    }
}
