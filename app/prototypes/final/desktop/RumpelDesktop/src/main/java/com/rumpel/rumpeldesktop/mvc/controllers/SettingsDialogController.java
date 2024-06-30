package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.fxutils.CheckBoxes;
import com.rumpel.rumpeldesktop.fxutils.prefs.Preferences;
import com.szaumoor.rumple.db.utils.Pair;
import com.szaumoor.rumple.utils.Money;
import com.szaumoor.utils.Property;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;
import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.infoAndWait;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * Controller for the settings dialog and all the functions
 */
public final class SettingsDialogController implements Initializable {
    @FXML
    private ComboBox<Currency> currComboBox;
    @FXML
    private ComboBox<Pair<String, String>> langComboBox;
    @FXML
    private Label outputLbl;
    @FXML
    private TextField outputTxt;
    @FXML
    private Button outputBtn;
    @FXML
    private CheckBox checkOutput;

    private final Pair<String, String> enLang = new Pair<>("en", getString("english"));
    private final Pair<String, String> esLang = new Pair<>("es", getString("spanish"));
    private final Pair<String, String> glLang = new Pair<>("gl", getString("galician"));
    private static final Logger logger = LogManager.getLogger(SettingsDialogController.class);
    private boolean langChanged; // this tracks whether the language has been changed to warn user restart of app is needed

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        config();
    }

    private void config() {
        langComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Pair<String, String> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || Objects.isNull(item)) setText(null);
                else setText(item.second());
            }
        });
        langComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Pair<String, String> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || Objects.isNull(item)) setText(null);
                else setText(item.second());
            }
        });
        currComboBox.setItems(observableArrayList(Money.currenciesSortedByCode()));
        currComboBox.setValue(Currency.getInstance(Preferences.instance.getDefaultCurrency()));
        langComboBox.setItems(observableArrayList(List.of(enLang, esLang, glLang)));
        var lang = Preferences.instance.getLanguage();
        langComboBox.setValue(langComboBox.getItems().stream().filter(l -> l.first().equals(lang)).findFirst().orElse(enLang));
        var initialLang = langComboBox.getValue();
        langComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            langChanged = !observable.getValue().equals(initialLang);
        });
        outputTxt.setText(Preferences.instance.getDefaultOutputDirectory());
        outputBtn.setOnAction(actionEvent -> {
            var dirChooser = new DirectoryChooser();
            dirChooser.setInitialDirectory(new File(Preferences.instance.getDefaultOutputDirectory()));
            var dir = dirChooser.showDialog(outputBtn.getScene().getWindow());
            if (dir != null) outputTxt.setText(dir.getAbsolutePath());
        });
        CheckBoxes.bindStateToNodes(checkOutput, false, outputTxt, outputLbl, outputBtn);
        checkOutput.setSelected(Preferences.instance.getDefaultOutputDirectory().equals(Property.currentPath()));
        outputTxt.setDisable(checkOutput.isSelected());
        outputLbl.setDisable(checkOutput.isSelected());
        outputBtn.setDisable(checkOutput.isSelected());
    }

    public void saveSettings() {
        Preferences.instance.setLanguage(langComboBox.getValue().first());
        Preferences.instance.setDefaultCurrency(currComboBox.getValue());
        var outputDir = new File(checkOutput.isSelected() ? Property.currentPath() : outputTxt.getText());
        Preferences.instance.setDefaultOutputDirectory(outputDir);
        if (langChanged) {
            infoAndWait(getString("restart_needed"));
        }
    }
}
