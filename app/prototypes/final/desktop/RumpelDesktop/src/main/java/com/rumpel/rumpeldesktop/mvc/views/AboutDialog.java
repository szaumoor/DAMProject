package com.rumpel.rumpeldesktop.mvc.views;

import com.rumpel.rumpeldesktop.fxutils.Clipboards;
import com.rumpel.rumpeldesktop.mvc.controllers.AboutDialogController;
import com.rumpel.rumpeldesktop.mvc.views.utils.ResourcePaths;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.util.Optional;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;

/**
 * Dialog class for About
 */
public final class AboutDialog extends AbstractDialog<AboutDialogController> {

    public AboutDialog(final Window owner) {
        super(owner, getString("about_rumpel"), ResourcePaths.ABOUT_DIALOG);
    }

    public void show() {
        final Optional<ButtonType> buttonType = dialog.showAndWait();
        if (buttonType.isPresent() && !buttonType.get().equals(ButtonType.OK)) {
            logger.info("Copied content to clipboard");
            Clipboards.copy(getController().getContent());
        }
    }

    @Override
    protected void handleButtons() {
        super.handleButtons();
        var button = (Button) pane.lookupButton(ButtonType.CANCEL);
        button.setText(getString("copy_close"));
    }
}
