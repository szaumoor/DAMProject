package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.db.DAOUser;
import com.rumpel.rumpeldesktop.fxutils.FXTaskRunner;
import com.rumpel.rumpeldesktop.fxutils.Tasks;
import com.rumpel.rumpeldesktop.fxutils.Translator;
import com.rumpel.rumpeldesktop.mvc.views.interfaces.TranslatableController;
import com.rumpel.rumpeldesktop.mvc.views.utils.ResourcePaths;
import com.rumpel.rumpeldesktop.mvc.views.utils.ViewUtils;
import com.szaumoor.rumple.model.entities.User;
import com.szaumoor.rumple.model.utils.types.Username;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.showAndWait;
import static javafx.scene.control.Alert.AlertType.ERROR;

public final class LoginFormController implements Initializable, TranslatableController, CredentialsScene {
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

    private String title;
    private String titleSignUp;
    private String wrongCreds;
    private String loginError;
    private String welcome;
    private String timedOut;

    private DAOUser dao;
    private boolean loading;

    private Stage stage;
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Login form initialized");
        setUpTranslations();
        userTxtField.setText("username");
        passField.setText("1martson1");
        dao = new DAOUser();
        showPassCheck.setOnAction(event -> {
            if (showPassCheck.isSelected()) {
                var pass = passField.getText();
                showPassField.setText(pass);
                showPassField.setVisible(true);
                passField.setVisible(false);
            } else {
                showPassField.setVisible(false);
                passField.setVisible(true);
            }
        });
    }

    @FXML
    public void login() {
        if (loading) return;
        logger.info("Attempting to login");
        Username username = new Username(userTxtField.getText());
        char[] pass = passField.getText().toCharArray();
        var ref = new AtomicReference<Optional<User>>();
        var worker = Tasks.createTask(() -> taskAuthenticationAndUI(ref, username, pass));
        FXTaskRunner taskRunner = new FXTaskRunner(List.of(worker), bool -> taskMonitorWorkerTask(bool, ref, username));
        taskRunner.execute(10, TimeUnit.SECONDS);
    }

    @FXML
    public void switchToSignUp() {
        if (loading) return;
        stage = (Stage) mainPane.getScene().getWindow();
        ViewUtils.switchScene(stage, ResourcePaths.SIGN_UP, titleSignUp, dimensions, false);
        logger.info("Switched to sign-up view");
    }

    @FXML
    public void enterPressed(final KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            logger.info("ENTER pressed in the window context");
            login();
        }
    }

    @Override
    public void setUpTranslations() {
        title = Translator.getString("rumpel");
        titleSignUp = Translator.getString("sign_up");
        wrongCreds = Translator.getString("wrong_creds");
        loginError = Translator.getString("login_error");
        welcome = Translator.getString("welcome");
        timedOut = Translator.getString("conn_timed_out");
    }

    private void taskAuthenticationAndUI(final AtomicReference<Optional<User>> ref, final Username username, final char[] pass) {
        signingProgress.setVisible(true);
        loading = true;
        ref.set(dao.authenticate(username, pass));
        loading = false;
        Arrays.fill(pass, '*');
        signingProgress.setVisible(false);
    }

    private void taskMonitorWorkerTask(final boolean bool, final AtomicReference<Optional<User>> ref, final Username username) {
        if (bool) {
            if (ref.get().isEmpty()) showAndWait(ERROR, wrongCreds, loginError, "");
            else {
                stage = (Stage) mainPane.getScene().getWindow();
                ViewUtils.switchScene(stage, ResourcePaths.HOME, title + " - " + welcome + ", " + username.get(), HomeController.dimensions, true);
                DAOUser.closeClient();
            }
        } else showAndWait(ERROR, timedOut, loginError, "");
    }
}
