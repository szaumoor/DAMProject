package com.rumpel.rumpeldesktop.mvc.views;

import com.rumpel.rumpeldesktop.mvc.controllers.ChartViewController;
import com.rumpel.rumpeldesktop.mvc.views.utils.ResourcePaths;
import com.szaumoor.rumple.model.entities.types.reports.ReportType;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.time.Month;
import java.time.Year;
import java.util.Currency;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;

/**
 * Dialog for chart viewing
 */
public class ChartsViewDialog extends AbstractDialog<ChartViewController> {
    private ChartsViewDialog(final Window owner) {
        super(owner, getString("reports"), ResourcePaths.CHARTS);
    }

    public ChartsViewDialog(final Window owner, final Currency curr, final Year year, final Month month, final ReportType reportType) {
        this(owner);
        getController().setParams(curr, year, month, reportType);
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
