package com.rumpel.rumpeldesktop.mvc.views;

import com.rumpel.rumpeldesktop.mvc.controllers.AccountDialogController;
import com.rumpel.rumpeldesktop.mvc.views.utils.ResourcePaths;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.util.Optional;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;

public class AccountDialog extends AbstractDialog<AccountDialogController> {

    public AccountDialog(final Window owner) {
        super(owner, getString("account"), ResourcePaths.ACCOUNT);
    }

    @Override
    public void show() {
        Optional<ButtonType> buttonType = dialog.showAndWait();
    }
}
