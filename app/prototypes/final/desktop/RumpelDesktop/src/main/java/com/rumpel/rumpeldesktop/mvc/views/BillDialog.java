package com.rumpel.rumpeldesktop.mvc.views;

import com.rumpel.rumpeldesktop.mvc.controllers.BillFormController;
import com.rumpel.rumpeldesktop.mvc.controllers.injectors.BillInjector;
import com.rumpel.rumpeldesktop.mvc.views.utils.ResourcePaths;
import com.szaumoor.rumple.model.entities.Bill;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.util.Optional;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;

/**
 * Dialog for bill creation and modification
 */
public class BillDialog extends AbstractDialog<BillFormController> {
    private final BillInjector injector;
    private boolean editMode = false;

    public BillDialog(final Window owner, final BillInjector injector, final Bill bill) {
        super(owner, getString("bill"), ResourcePaths.NEW_BILL);
        this.injector = injector;
        getController().setBill(bill);
        if (bill != null) editMode = true;
    }

    @Override
    protected void handleButtons() {
        super.handleButtons();
        pane.getButtonTypes().removeIf(buttonType -> buttonType == ButtonType.CANCEL);
    }

    @Override
    public void show() {
        final Optional<ButtonType> buttonType = dialog.showAndWait();
        if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
            Bill bill = getController().getBill();
            if (bill != null && bill.size() > 0) {
                if (editMode) injector.modifyBill(bill);
                else injector.insertBill(bill);
            }

        }

    }
}
