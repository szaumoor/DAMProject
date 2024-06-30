package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.db.DAOUser;
import com.rumpel.rumpeldesktop.db.AbstractDAO;
import com.rumpel.rumpeldesktop.fxutils.*;
import com.rumpel.rumpeldesktop.fxutils.threads.FXTaskRunner;
import com.rumpel.rumpeldesktop.mvc.views.utils.ResourcePaths;
import com.szaumoor.rumple.model.entities.User;

import com.szaumoor.rumple.model.entities.types.UserEmail;
import com.szaumoor.rumple.model.entities.types.Username;
import com.szaumoor.rumple.model.interfaces.UserPrimaryField;
import com.szaumoor.utils.Strings;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;
import static com.rumpel.rumpeldesktop.fxutils.Dimensions.CREDENTIALS_DIMENSIONS;
import static com.rumpel.rumpeldesktop.fxutils.Dimensions.HOME_DIMENSIONS;
import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.errorAndWait;
import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.showAndWait;
import static com.rumpel.rumpeldesktop.mvc.views.utils.ViewUtils.switchScene;
import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * Controller for the login form
 */
public final class LoginFormController implements Initializable {
    @FXML
    private Button loginBtn;
    @FXML
    private Button switchToSignUpBtn;
    @FXML
    private Button goToSignUpBtn;
    @FXML
    private ProgressIndicator signingProgress;
    @FXML
    private TextField userTxtField;
    @FXML
    private PasswordField passField;
    @FXML
    private HBox mainPane;
    @FXML
    private CheckBox showPassCheck;

    @FXML
    private TextField showPassField;

    private DAOUser dao;
    private Stage stage;
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Login form initialized");
        CheckBoxes.setUpForPasswords(showPassCheck, Map.of(passField, showPassField));
        dao = new DAOUser();
        Nodes.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.FORWARD) switchToSignUp();
                },
                passField, showPassField, userTxtField, showPassCheck, goToSignUpBtn, mainPane, switchToSignUpBtn, loginBtn);
        Styles.applyFontSize(15, showPassCheck, goToSignUpBtn);
        Buttons.setIconButton(goToSignUpBtn, "arrow_to_sign_up.png");
        Platform.runLater(() -> mainPane.getScene().getWindow().setOnCloseRequest(event -> Tasks.exit()));
    }

    @FXML
    public void login() {
        logger.info("Attempting to login");
        String name = userTxtField.getText();
        String passStr = passField.getText();
        if (!Strings.hasContent(name, passStr)) {
            errorAndWait(getString("wrong_creds"));
            return;
        }
        UserPrimaryField field;
        if (UserEmail.validate(name)) field = new UserEmail(name);
        else field = new Username(name);

        var pass = passStr.toCharArray();
        var ref = new AtomicReference<Optional<User>>(Optional.empty());
        var worker = Tasks.createTask(() -> taskAuthenticationAndUI(ref, field, pass));
        var taskRunner = new FXTaskRunner(worker, bool -> taskMonitorWorkerTask(bool, ref));
        taskRunner.execute(10, TimeUnit.SECONDS);
    }

    @FXML
    public void switchToSignUp() {
        stage = (Stage) mainPane.getScene().getWindow();
        switchScene(stage, ResourcePaths.SIGN_UP, getString("sign_up"), CREDENTIALS_DIMENSIONS, false);
        logger.info("Switched to sign-up view");
    }

    @FXML
    public void enterPressed(final KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            logger.info("ENTER pressed in the window context");
            login();
        }
    }

    private void taskAuthenticationAndUI(final AtomicReference<Optional<User>> ref, final UserPrimaryField primaryField, final char[] pass) {
        signingProgress.setVisible(true);
        Nodes.disable(loginBtn, switchToSignUpBtn, goToSignUpBtn);
        ref.set(dao.authenticate(primaryField, pass));
        Arrays.fill(pass, '*');
        signingProgress.setVisible(false);
    }

    private void taskMonitorWorkerTask(final boolean finishedInTime, final AtomicReference<Optional<User>> ref) {
        if (finishedInTime) {
            if (ref.get().isEmpty()) {
                signingProgress.setVisible(false);
                Nodes.enable(loginBtn, switchToSignUpBtn, goToSignUpBtn);
                showAndWait(ERROR, getString("wrong_creds"), getString("login_error"), "");
            } else {
                AbstractDAO.closeClient();
                stage = (Stage) mainPane.getScene().getWindow();
                switchScene(stage, ResourcePaths.HOME, getString("rumpel") + " - " + getString("welcome") + ", " + ref.get().get().getUsername().value(), HOME_DIMENSIONS, true);
            }
        } else errorAndWait(getString("conn_timed_out"));
    }
}
