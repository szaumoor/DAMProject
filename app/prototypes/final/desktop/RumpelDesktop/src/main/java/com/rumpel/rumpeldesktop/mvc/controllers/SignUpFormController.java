package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.db.DAOUser;
import com.rumpel.rumpeldesktop.fxutils.*;
import com.rumpel.rumpeldesktop.mvc.views.utils.ResourcePaths;
import com.rumpel.rumpeldesktop.mvc.views.utils.Tooltips;
import com.rumpel.rumpeldesktop.mvc.views.utils.ViewUtils;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.User;
import com.szaumoor.rumple.model.entities.types.UserEmail;
import com.szaumoor.rumple.model.entities.types.UserPass;
import com.szaumoor.rumple.model.entities.types.Username;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import static com.rumpel.rumpeldesktop.fxutils.Dimensions.CREDENTIALS_DIMENSIONS;
import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;
import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.*;
import static com.szaumoor.utils.Optionals.anyEmpty;
import static javafx.scene.control.Alert.AlertType.ERROR;

public final class SignUpFormController implements Initializable {
    @FXML
    private Button signUpBtn;
    @FXML
    private Button goBackBtn;
    @FXML
    private Button switchToLoginBtn;
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

    private DAOUser dao;
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        logger.info("Sign up form controller initialized");
        userTxtField.setTooltip(Tooltips.getDefaultTooltip(getString("username_tooltip")));
        passField.setTooltip(Tooltips.getDefaultTooltip(getString("password_tooltip")));
        dao = new DAOUser();
        Buttons.setIconButton(goBackBtn, "arrow_to_login.png");
        Nodes.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.BACK) switchToLogin();
                },
                passField, showPassField, userTxtField, showPassCheck, switchToLoginBtn,
                mainPane, signUpBtn, goBackBtn, emailField, passFieldConfirm);
        Styles.applyFontSize(15, showPassCheck, switchToLoginBtn);
        CheckBoxes.setUpForPasswords(showPassCheck, Map.of(passField, showPassField, passFieldConfirm, showPassFieldConfirm));
        Platform.runLater(() -> mainPane.getScene().getWindow().setOnCloseRequest(event -> Tasks.exit()));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    // checked with the anyEmpty() method from my library -- IDE does not detect it
    @FXML
    public void signUp() {
        logger.info("Started process to sign up");
        var user = userTxtField.getText();
        var emailTxt = emailField.getText();
        var passTxt = passField.getText();
        var passAgain = passFieldConfirm.getText();

        if (UserEmail.validate(user)) {
            showAndWait(ERROR, getString("username_email_error"), getString("sign_up_error"), "");
            return;
        }
        if (!passTxt.equals(passAgain))
            showAndWait(ERROR, getString("passwords_not_match"), getString("sign_up_error"), "");
        else {
            var username = Username.of(user);
            var email = UserEmail.of(emailTxt);
            var pass = UserPass.of(passTxt.toCharArray());
            if (anyEmpty(username, email, pass)) errorAndWait(getString("invalid_fields"));
            else {
                var optionalUser = User.of(username.get(), email.get(), pass.get());
                if (optionalUser.isPresent()) {
                    Nodes.disable(switchToLoginBtn, signUpBtn, goBackBtn);
                    Outcome outcome = dao.insert(optionalUser.get());
                    if (outcome == Outcome.SUCCESS) {
                        Nodes.enable(switchToLoginBtn, signUpBtn, goBackBtn);
                        infoAndWait(getString("user_created"));
                        logger.info("user " + optionalUser.get().getUsername().value() + " inserted.");
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
        ViewUtils.switchScene(stage, ResourcePaths.LOGIN, getString("login"), CREDENTIALS_DIMENSIONS, false);
        logger.info("Switched to login view");
    }
}
