package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.db.DAOBill;
import com.rumpel.rumpeldesktop.db.DAOTag;
import com.rumpel.rumpeldesktop.db.DAOUser;
import com.szaumoor.rumple.model.entities.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;
import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.*;
import static com.szaumoor.rumple.db.utils.Outcome.SUCCESS;

/**
 * Controller for the tags dialog and all the functions
 */
public final class TagsDialogController implements Initializable {
    @FXML
    private TextField newTagField;
    @FXML
    private ComboBox<Tag> tagComboBox;

    private DAOTag dao;
    private ObservableList<Tag> tagObservableList;
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dao = new DAOTag();
        tagComboBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Tag> call(ListView<Tag> param) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(Tag item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else setText(item.getName());
                    }
                };
            }
        });
        tagObservableList = FXCollections.observableArrayList(dao.getAll());
        tagComboBox.setItems(tagObservableList);
        tagComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (observable.getValue() != null) newTagField.setText(observable.getValue().getName());
        });
    }

    @FXML
    public void insertTag() {
        final var tagName = newTagField.getText();
        if (tagName.isBlank()) {
            errorAndWait(getString("cannot_insert_empty_field"));
            return;
        } else {
            final Optional<Tag> op = dao.get(tagName);
            if (op.isPresent()) errorAndWait(getString("tag_exists_already"));
            else {
                var tag = Tag.of(tagName);
                if (tag.isPresent()) {
                    Tag extracted = tag.get();
                    extracted.setUser(DAOUser.getLoggedUser());
                    var insert = dao.insert(extracted);
                    if (insert == SUCCESS) {
                        tagObservableList.add(extracted);
                        infoAndWait(getString("tag_inserted"));
                    } else errorAndWait(getString("connection_error"));
                } else logger.error("Tag could not be created somehow, re-check conditional validation of tags");
            }
        }
        newTagField.clear();
    }

    @FXML
    public void modifyTag() {
        if (tagComboBox.getSelectionModel().getSelectedIndex() == -1) errorAndWait(getString("no_item_selected"));
        else {
            var selectedTag = tagComboBox.getSelectionModel().getSelectedItem();
            if (!selectedTag.setName(newTagField.getText())) errorAndWait(getString("tag_invalid_params"));
            else {
                var out = dao.modify(selectedTag);
                if (out == SUCCESS) {
                    infoAndWait(getString("tag_modified"));
                    tagComboBox.getSelectionModel().select(-1);
                    tagComboBox.getSelectionModel().select(selectedTag);
                } else logger.error("Some unknown error occurred...");
            }
        }
    }

    @FXML
    public void deleteTag() {
        if (tagComboBox.getSelectionModel().getSelectedIndex() == -1) errorAndWait(getString("no_item_selected"));
        else {
            var buttonType = confirmAndWait(getString("confirmation_delete_tag"));
            if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {
                var selectedTag = tagComboBox.getSelectionModel().getSelectedItem();
                if (new DAOBill().billWithTagExists(selectedTag)) {
                    errorAndWait(getString("tag_in_use"));
                    return;
                }
                var out = dao.delete(selectedTag);
                if (out == SUCCESS) {
                    tagObservableList.remove(selectedTag);
                } else logger.error("Some unknown error occurred...");
            }
        }
    }
}
