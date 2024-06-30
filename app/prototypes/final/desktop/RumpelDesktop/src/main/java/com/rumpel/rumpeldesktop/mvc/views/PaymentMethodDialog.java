package com.rumpel.rumpeldesktop.mvc.views;

import com.rumpel.rumpeldesktop.mvc.controllers.PaymentMethodController;
import com.rumpel.rumpeldesktop.mvc.views.utils.ResourcePaths;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;

/**
 * Dialog for payment method creation and modification
 */
public class PaymentMethodDialog extends AbstractDialog<PaymentMethodController> {

    public PaymentMethodDialog(final Window owner) {
        super(owner, getString("payment_methods"), ResourcePaths.PM_DIALOG);
    }

    @Override
    protected void handleButtons() {
        super.handleButtons();
        pane.getButtonTypes().removeIf(buttonType -> buttonType == ButtonType.CANCEL);
    }

    public void show() {
        dialog.showAndWait();
    }

}
