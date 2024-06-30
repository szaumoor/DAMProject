package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.db.DAOUser;
import com.rumpel.rumpeldesktop.db.utils.Outcome;
import com.rumpel.rumpeldesktop.fxutils.Translator;
import com.rumpel.rumpeldesktop.mvc.views.interfaces.TranslatableController;
import com.rumpel.rumpeldesktop.mvc.views.utils.ResourcePaths;
import com.rumpel.rumpeldesktop.mvc.views.utils.ViewUtils;

import com.szaumoor.rumple.model.entities.User;
import com.szaumoor.rumple.model.utils.types.UserEmail;
import com.szaumoor.rumple.model.utils.types.UserPass;
import com.szaumoor.rumple.model.utils.types.Username;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.showAndWait;
import static com.szaumoor.rumple.model.utils.Bools.anyFalse;
import static javafx.scene.control.Alert.AlertType.*;


public final class SignUpFormController implements TranslatableController, Initializable, CredentialsScene {

    @FXML
    private TextField userTxtField;
    @FXML
    private PasswordField passField;
    @FXML
    private PasswordField passFieldConfirm;
    @FXML
    private TextField emailField;
    @FXML
    private HBox mainPane;

    private String title;
    private String usernameToolTip;
    private String passwordTooltip;

    private String alertTitle;
    private String unmatched;

    private String confirmationAlertTitle;
    private String contentConfirmationAlert;
    private String invalidFields;

    private DAOUser dao;

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        logger.info("Sign up form controller initialized");
        setUpTranslations();
        userTxtField.setTooltip(new Tooltip(usernameToolTip));
        passField.setTooltip(new Tooltip(passwordTooltip));
        dao = new DAOUser();
    }

    @Override
    public void setUpTranslations() {
        title = Translator.getString("login");
        usernameToolTip = Translator.getString("username_tooltip");
        passwordTooltip = Translator.getString("password_tooltip");
        alertTitle = Translator.getString("sign_up_error");
        unmatched = Translator.getString("passwords_not_match");
        confirmationAlertTitle = Translator.getString("success");
        contentConfirmationAlert = Translator.getString("user_created");
        invalidFields = Translator.getString("invalid_fields");
    }

    @FXML
    public void signUp() {
        logger.info("Started process to sign up");
        var user = userTxtField.getText();
        var emailTxt = emailField.getText();
        var passTxt = passField.getText();
        var passAgain = passFieldConfirm.getText();

        if (!passTxt.equals(passAgain)) showAndWait(ERROR, unmatched, alertTitle, "");
        else {
            var username = Username.of(user);
            var email = UserEmail.of(emailTxt);
            var pass = UserPass.of(passTxt.toCharArray());

            if (anyFalse(username.isPresent(), email.isPresent(), pass.isPresent())) showAndWait(ERROR, invalidFields, alertTitle, "");
            else {
                var optionalUser = User.of(username.get(), email.get(), pass.get());
                if (optionalUser.isPresent()) {
                    Outcome outcome = dao.insert(optionalUser.get());
                    if (outcome == Outcome.SUCCESS) {
                        showAndWait(CONFIRMATION, contentConfirmationAlert, confirmationAlertTitle, "");
                        logger.info("user " + optionalUser.get().getUsername().get() + " inserted.");
                    } else if (outcome == Outcome.UNIQUE_EXISTS) logger.warn("User found already, sorry");
                    else logger.error("User could not be inserted somehow");
                } else logger.error("User object could not get created");
            }
        }
    }

    @FXML
    public void enterPressed(final KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            logger.info("ENTER pressed in the window context");
            signUp();
        }
    }

    @FXML
    public void switchToLogin() {
        final Stage stage = (Stage) mainPane.getScene().getWindow();
        ViewUtils.switchScene(stage, ResourcePaths.LOGIN, title, dimensions, false);
        logger.info("Switched to login view");
    }
}
