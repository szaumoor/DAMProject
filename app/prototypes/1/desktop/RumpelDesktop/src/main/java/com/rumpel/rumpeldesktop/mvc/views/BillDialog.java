package com.rumpel.rumpeldesktop.mvc.views;

import com.rumpel.rumpeldesktop.mvc.controllers.BillFormController;
import com.rumpel.rumpeldesktop.mvc.views.interfaces.AbstractDialog;
import com.rumpel.rumpeldesktop.mvc.views.utils.ResourcePaths;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.util.Optional;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;

public class BillDialog extends AbstractDialog<BillFormController> {

    public BillDialog(final Window owner) {
        super(owner, getString("bill"), ResourcePaths.NEW_BILL);
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
