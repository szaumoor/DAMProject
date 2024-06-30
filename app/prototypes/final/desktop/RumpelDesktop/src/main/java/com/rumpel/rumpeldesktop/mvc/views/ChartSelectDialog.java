package com.rumpel.rumpeldesktop.mvc.views;

import com.rumpel.rumpeldesktop.mvc.controllers.ChartSelectionController;
import com.rumpel.rumpeldesktop.mvc.views.utils.ResourcePaths;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;

/**
 * Dialog for chart selection
 */
public class ChartSelectDialog extends AbstractDialog<ChartSelectionController> {
    public ChartSelectDialog(Window owner) {
        super(owner, getString("new_report"), ResourcePaths.CHART_SELECTION);
    }

    @Override
    protected void handleButtons() {
        super.handleButtons();
        pane.getButtonTypes().removeIf(buttonType -> buttonType == ButtonType.CANCEL);
    }

    @Override
    public void show() {
        dialog.showAndWait();
    }
}
