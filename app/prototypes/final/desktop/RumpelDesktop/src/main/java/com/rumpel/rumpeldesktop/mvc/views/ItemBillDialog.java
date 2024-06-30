package com.rumpel.rumpeldesktop.mvc.views;

import com.rumpel.rumpeldesktop.mvc.controllers.ItemBillDialogController;
import com.rumpel.rumpeldesktop.mvc.controllers.injectors.ItemInjector;
import com.rumpel.rumpeldesktop.mvc.views.utils.ResourcePaths;
import com.szaumoor.rumple.model.entities.Bill;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.util.Optional;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;

/**
 * Dialog for the creation or modification of items in a bill
 */
public final class ItemBillDialog extends AbstractDialog<ItemBillDialogController> {

    private final ItemInjector injector;

    public ItemBillDialog(final Window owner, final ItemInjector injector, final Bill bill) {
        super(owner, getString("bill_items"), ResourcePaths.ITEM_BILL_DIALOG);
        this.injector = injector;
        getController().setBill(bill);
    }


    @Override
    public void show() {
        final Optional<ButtonType> buttonType = dialog.showAndWait();
        if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
            logger.info("tapped the ok button");
            injector.insertItems(getController().getList());
        }
    }
}
