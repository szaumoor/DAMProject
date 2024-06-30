package com.rumpel.rumpeldesktop.mvc.controllers;

import com.rumpel.rumpeldesktop.db.DAOUser;
import com.rumpel.rumpeldesktop.fxutils.CheckBoxes;
import com.rumpel.rumpeldesktop.mvc.views.utils.Tooltips;
import com.rumpel.rumpeldesktop.mvc.views.utils.Alerts;
import com.szaumoor.rumple.db.utils.Outcome;
import com.szaumoor.rumple.model.entities.User;
import com.szaumoor.rumple.model.entities.types.UserPass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;
import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.errorAndWait;
import static com.rumpel.rumpeldesktop.mvc.views.utils.Alerts.infoAndWait;
import static com.szaumoor.utils.Strings.hasContent;

/**
 * Controller for the account dialog and all the functions
 */
public final class AccountDialogController implements Initializable {

    private final User user = DAOUser.getLoggedUser();
    @FXML
    private TextField showPassField;
    @FXML
    private TextField showNewPassField;
    @FXML
    private TextField showConfirmPassField;
    @FXML
    private PasswordField currentPassField;
    @FXML
    private PasswordField newPassField;
    @FXML
    private PasswordField confirmPassField;
    @FXML
    private CheckBox showPassCheck;

    private final Logger logger = LogManager.getLogger();
    private DAOUser dao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dao = new DAOUser();
        confirmPassField.setTooltip(Tooltips.getDefaultTooltip(getString("password_tooltip")));
        newPassField.setTooltip(Tooltips.getDefaultTooltip(getString("password_tooltip")));
        CheckBoxes.setUpForPasswords(showPassCheck, Map.of(
                currentPassField, showPassField,
                newPassField, showNewPassField,
                confirmPassField, showConfirmPassField
        ));
    }

    @FXML
    public void changePass() {
        var current = currentPassField.getText();
        var newPass = newPassField.getText();
        var confirmPass = confirmPassField.getText();

        if (!hasContent(current, newPass, confirmPass)) {
            errorAndWait(getString("missing_fields"));
            return;
        }

        if (!user.getUserPass().verify(current)) {
            errorAndWait(getString("verification_failed"));
            return;
        }
        logger.info("Password verified, attempting the password change now...");
        if (!newPass.equals(confirmPass)) {
            errorAndWait(getString("passwords_not_match"));
            return;
        }

        var newUserPass = UserPass.of(newPass.toCharArray());
        if (newUserPass.isEmpty()) {
            errorAndWait("password_tooltip");
            return;
        }
        logger.info("All verifications complete, attempting modification now");
        user.setUserPass(newUserPass.get());
        var out = dao.modify(user);
        if (out == Outcome.SUCCESS) infoAndWait(getString("password_changed"));
        else logger.error("Some unknown error occurred when attempting to modify user password");
    }

    @FXML
    public void closeAccount() {
       var buttonType = Alerts.confirmAndWait(getString("confirmation_delete_user"));
        if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {
            Outcome delete = new DAOUser().delete(user);
            if (delete == Outcome.NULL) logger.error("Oh no, null value");
            else if (delete == Outcome.SUCCESS) {
                logger.info("User " + user.getUsername().value() + " deleted");
                infoAndWait(getString("account_deleted"));
                DAOUser.logout();
            } else
                logger.error("Some unknown error occurred when attempting to delete user " + user.getUsername().value());
        }
    }
}
