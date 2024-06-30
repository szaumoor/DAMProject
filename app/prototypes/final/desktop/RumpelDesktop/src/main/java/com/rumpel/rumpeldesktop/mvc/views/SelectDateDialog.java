package com.rumpel.rumpeldesktop.mvc.views;

import com.rumpel.rumpeldesktop.mvc.controllers.SelectMonthDialogController;
import com.rumpel.rumpeldesktop.mvc.controllers.injectors.DateInjector;
import com.rumpel.rumpeldesktop.mvc.views.utils.ResourcePaths;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.time.Month;
import java.time.Year;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;

/**
 * Dialog for selecting month and year for the purposes of changing the current view in the main window
 */
public class SelectDateDialog extends AbstractDialog<SelectMonthDialogController> {
    private final DateInjector dateInjector;

    public SelectDateDialog(final Window owner, final DateInjector dateInjector, final Year year, final Month month) {
        super(owner, getString("select_date"), ResourcePaths.SELECT_MONTH);
        this.dateInjector = dateInjector;
        getController().setMonth(year, month);
    }

    @Override
    public void show() {
        var btn = dialog.showAndWait();
        if (btn.isPresent() && btn.get() == ButtonType.OK) {
            var pair = getController().getMonth();
            dateInjector.setDate(pair.first(), pair.second());
        }
    }
}
