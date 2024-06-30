package com.rumpel.rumpeldesktop.mvc.views.interfaces;

import com.rumpel.rumpeldesktop.App;
import com.rumpel.rumpeldesktop.fxutils.Translator;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;
import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.showAndWait;
import static javafx.scene.control.Alert.AlertType.ERROR;

public abstract class AbstractDialog<T extends Initializable> {

    protected static final Logger logger = LogManager.getLogger();
    protected final DialogPane pane;
    protected final Dialog<ButtonType> dialog;
    private final T controller;

    protected AbstractDialog(final Window owner, final String title, final String resource) {
        dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.setTitle(title);

        final FXMLLoader loader = new FXMLLoader(App.class.getResource(resource), Translator.getBundle());
        pane = dialog.getDialogPane();
        final Window window = pane.getScene().getWindow();

        try {
            pane.setContent(loader.load());
        } catch (IOException e) {
            showAndWait(ERROR, getString("error_loading_dialog"), getString("error"), "");
            logger.info(e.fillInStackTrace());
        }
        controller = loader.getController();
        window.setOnCloseRequest(event -> window.hide());
        handleButtons();
    }

    protected void handleButtons() {
        ObservableList<ButtonType> buttonTypes = pane.getButtonTypes();
        buttonTypes.add(ButtonType.OK);
        buttonTypes.add(ButtonType.CANCEL);
        ObservableList<String> styleClass = pane.getStyleClass();
        styleClass.add("panel");
        styleClass.add("panel-info");

        Button btnOK = (Button) pane.lookupButton(ButtonType.OK);
        ObservableList<String> styleClassOK = btnOK.getStyleClass();
        styleClassOK.add("btn");
        styleClassOK.add("btn-success");

        Button btnCopyClose = (Button) pane.lookupButton(ButtonType.CANCEL);
        ObservableList<String> styleClassCopyClose = btnCopyClose.getStyleClass();
        styleClassCopyClose.add("btn");
        styleClassCopyClose.add("btn-success");
    }

    protected abstract void show();

    public T getController() {
        return controller;
    }
}
