package com.rumpel.rumpeldesktop.mvc.views;

import com.rumpel.rumpeldesktop.mvc.controllers.SettingsDialogController;
import com.rumpel.rumpeldesktop.mvc.views.utils.ResourcePaths;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;

/**
 * Dialog for settings
 */
public class SettingsDialog extends AbstractDialog<SettingsDialogController> {
    public SettingsDialog(final Window owner) {
        super(owner, getString("settings"), ResourcePaths.SETTINGS);
    }

    @Override
    public void show() {
        var buttonType = dialog.showAndWait();
        if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {
            getController().saveSettings();
        }
    }
}
