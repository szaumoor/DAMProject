package com.rumpel.rumpeldesktop.mvc.views;

import com.rumpel.rumpeldesktop.mvc.controllers.BudgetDialogController;
import com.rumpel.rumpeldesktop.mvc.views.utils.ResourcePaths;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.util.Optional;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;

public final class BudgetDialog extends AbstractDialog<BudgetDialogController> {

    public BudgetDialog(final Window owner) {
        super(owner, getString("budgets"), ResourcePaths.BUDGETS_DIALOG);
    }

    @Override
    public void show() {
        final Optional<ButtonType> buttonType = dialog.showAndWait();
        if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK))
            logger.info("tapped the ok button");
        else {

        }
    }

}
