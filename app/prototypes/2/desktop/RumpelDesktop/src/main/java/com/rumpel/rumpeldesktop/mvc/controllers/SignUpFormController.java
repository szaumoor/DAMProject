package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.db.DAOUser;
import com.rumpel.rumpeldesktop.fxutils.*;
import com.rumpel.rumpeldesktop.mvc.controllers.interfaces.CredentialsScene;
import com.rumpel.rumpeldesktop.mvc.controllers.interfaces.TranslatableController;
import com.rumpel.rumpeldesktop.mvc.views.utils.ResourcePaths;
import com.rumpel.rumpeldesktop.mvc.views.utils.ViewUtils;

import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.User;

import com.szaumoor.rumple.model.entities.types.UserEmail;
import com.szaumoor.rumple.model.entities.types.UserPass;
import com.szaumoor.rumple.model.entities.types.Username;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.*;
import static com.szaumoor.rumple.utils.Optionals.anyEmpty;
import static javafx.scene.control.Alert.AlertType.*;


public final class SignUpFormController implements TranslatableController, Initializable, CredentialsScene {
    @FXML
    private TextField showPassField;
    @FXML
    private TextField showPassFieldConfirm;
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

    @FXML
    private CheckBox showPassCheck;

    private String title;
    private String usernameToolTip;
    private String passwordTooltip;

    private String alertTitle;
    private String unmatched;

    private String contentConfirmationAlert;
    private String invalidFields;

    private DAOUser dao;

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        logger.info("Sign up form controller initialized");
        setUpTranslations();
        userTxtField.setTooltip(Tooltips.getDefaultTooltip(usernameToolTip));
        passField.setTooltip(Tooltips.getDefaultTooltip(passwordTooltip));
        dao = new DAOUser();
        CheckBoxes.setUpForPasswords(showPassCheck, Map.of(passField, showPassField, passFieldConfirm, showPassFieldConfirm));
        Platform.runLater(() -> mainPane.getScene().getWindow().setOnCloseRequest(event -> Tasks.exit()));
    }

    @Override
    public void setUpTranslations() {
        title = Translator.getString("login");
        usernameToolTip = Translator.getString("username_tooltip");
        passwordTooltip = Translator.getString("password_tooltip");
        alertTitle = Translator.getString("sign_up_error");
        unmatched = Translator.getString("passwords_not_match");
        contentConfirmationAlert = Translator.getString("user_created");
        invalidFields = Translator.getString("invalid_fields");
    }

    @FXML
    public void signUp() {
        logger.info("Started process to sign up");
        var user = userTxtField.getText();
        var emailTxt = emailField.getText();
        var passTxt = passField.getText();
        var passAgain =passFieldConfirm.getText();

        if (!passTxt.equals(passAgain)) showAndWait(ERROR, unmatched, alertTitle, "");
        else {
            var username = Username.of(user);
            var email = UserEmail.of(emailTxt);
            var pass = UserPass.of(passTxt.toCharArray());
            if (anyEmpty(username, email, pass)) errorAndWait(invalidFields);
            else {
                var optionalUser = User.of(username.get(), email.get(), pass.get());
                if (optionalUser.isPresent()) {
                    Outcome outcome = dao.insert(optionalUser.get());
                    if (outcome == Outcome.SUCCESS) {
                        confirmAndWait(contentConfirmationAlert);
                        logger.info("user " + optionalUser.get().getUsername().username() + " inserted.");
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
        final var stage = (Stage) mainPane.getScene().getWindow();
        ViewUtils.switchScene(stage, ResourcePaths.LOGIN, title, dimensions, false);
        logger.info("Switched to login view");
    }
}
