package com.rumpel.rumpeldesktop.mvc.views;

import com.rumpel.rumpeldesktop.db.DAOUser;
import com.rumpel.rumpeldesktop.mvc.controllers.AccountDialogController;
import com.rumpel.rumpeldesktop.mvc.views.utils.ResourcePaths;
import com.rumpel.rumpeldesktop.mvc.views.utils.ViewUtils;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.Window;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;

/**
 * Dialog class for Account
 */
public class AccountDialog extends AbstractDialog<AccountDialogController> {

    public AccountDialog(final Window owner) {
        super(owner, getString("account"), ResourcePaths.ACCOUNT);
    }


    @Override
    protected void handleButtons() {
        super.handleButtons();
        pane.getButtonTypes().removeIf(buttonType -> buttonType == ButtonType.CANCEL);
    }

    @Override
    public void show() {
        dialog.showAndWait();
        if (DAOUser.getLoggedUser() == null) {
            Stage owner = (Stage) dialog.getOwner();
            ViewUtils.showLoginView(owner);
        }
    }
}
