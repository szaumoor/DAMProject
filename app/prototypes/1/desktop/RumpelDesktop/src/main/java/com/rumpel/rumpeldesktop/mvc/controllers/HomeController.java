package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.db.DAOUser;
import com.rumpel.rumpeldesktop.fxutils.Translator;
import com.rumpel.rumpeldesktop.mvc.views.*;
import com.rumpel.rumpeldesktop.mvc.views.interfaces.TranslatableController;
import com.rumpel.rumpeldesktop.mvc.views.utils.*;

import com.szaumoor.rumple.model.entities.Bill;
import com.szaumoor.rumple.model.entities.User;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public final class HomeController implements TranslatableController, Initializable {

    public static final Dimension dimensions = new Dimension(new Width(1024), new Height(800));

    private static final Logger logger = LogManager.getLogger();

    @FXML
    private TableView<Bill> tableView;

    public static User user;

    @FXML
    private BorderPane mainPane;

    private String title;

    @FXML
    private void closeApp() {
        logger.info("Closing app");
        Platform.exit();
    }

    @FXML
    private void logout() {
        final Stage stage = (Stage) mainPane.getScene().getWindow();
        stage.setFullScreen(false);
        new DAOUser().logout();
        logger.info("User " + user.getUsername().get() + " logged out at local date: " + LocalDateTime.now());
        ViewUtils.switchScene(stage, ResourcePaths.LOGIN, title, CredentialsScene.dimensions, false);
    }

    @FXML
    private void openAboutDialog() {
        logger.info("Opened 'About' dialog");
        new AboutDialog(mainPane.getScene().getWindow()).show();
    }

    @FXML
    private void openTagsForm() {
        new TagDialog(mainPane.getScene().getWindow()).show();
    }

    @FXML
    public void openPmForm() {
        new PaymentMethodDialog(mainPane.getScene().getWindow()).show();
    }

    @Override
    public void setUpTranslations() {
        title = Translator.getString("rumpel");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpTranslations();
        Optional<User> loggedUser = DAOUser.getLoggedUser();
        if (loggedUser.isEmpty()) {
            logger.fatal("Couldn't find authenticated user");
            throw new RuntimeException("Critical error: couldn't find authenticated user");
        }
        user = loggedUser.get();

    }

    @FXML
    public void openBudgetsForm() {
        new BudgetDialog(mainPane.getScene().getWindow()).show();
    }

    @FXML
    public void openNewBillDialog() {
        new BillDialog(mainPane.getScene().getWindow()).show();
    }

    @FXML
    public void openAccountDialog() {
        new AccountDialog(mainPane.getScene().getWindow()).show();
    }
}