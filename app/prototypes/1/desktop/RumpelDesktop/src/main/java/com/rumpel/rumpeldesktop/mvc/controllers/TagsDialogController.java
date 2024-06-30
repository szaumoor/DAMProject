package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.db.DAOTag;
import com.rumpel.rumpeldesktop.db.DAOUser;
import com.rumpel.rumpeldesktop.db.utils.Outcome;
import com.rumpel.rumpeldesktop.mvc.views.utils.Alerts;
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

import static com.rumpel.rumpeldesktop.db.utils.Outcome.SUCCESS;
import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

public final class TagsDialogController implements Initializable {

    @FXML
    private TextField newTagField;

    @FXML
    private ComboBox<Tag> tagComboBox;

    private DAOTag dao;

    private static final Logger logger = LogManager.getLogger();
    private ObservableList<Tag> tagObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dao = new DAOTag(DAOUser.getLoggedUser().get());
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
    }

    @FXML
    public void insertTag() {
        final var tagName = newTagField.getText();
        if (tagName.isBlank()) {
            Alerts.showAndWait(ERROR, getString("cannot_insert_empty_field"), getString("error"), "");
            return;
        }
        final Optional<Tag> op = dao.get(tagName);
        if (op.isPresent()) {
            Alerts.showAndWait(ERROR, getString("tag_exists_already"), getString("error"), "");
        } else {
            Optional<Tag> tag = Tag.of(tagName);
            if (tag.isPresent()) {
                Tag extracted = tag.get();
                extracted.setUser(DAOUser.getLoggedUser().get());
                Outcome insert = dao.insert(extracted);
                if (insert == SUCCESS) {
                    tagObservableList.add(extracted);
                    Alerts.showAndWait(INFORMATION, getString("tag_inserted"), null, "");
                }
                else Alerts.showAndWait(INFORMATION, getString("connection_error"), getString("error"), "");
            }
            else logger.error("Tag could not be created somehow, re-check conditional validation of tags");
        }
        newTagField.clear();
    }
}
