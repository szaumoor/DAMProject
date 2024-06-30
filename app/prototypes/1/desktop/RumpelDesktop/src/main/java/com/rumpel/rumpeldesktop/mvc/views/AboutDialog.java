package com.rumpel.rumpeldesktop.mvc.views;

import com.rumpel.rumpeldesktop.fxutils.Clipboards;
import com.rumpel.rumpeldesktop.mvc.controllers.AboutDialogController;
import com.rumpel.rumpeldesktop.mvc.views.interfaces.AbstractDialog;
import com.rumpel.rumpeldesktop.mvc.views.utils.ResourcePaths;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.util.Optional;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;

public final class AboutDialog extends AbstractDialog<AboutDialogController> {

    public AboutDialog(final Window owner ) {
        super(owner, getString("about_rumpel"), ResourcePaths.ABOUT_DIALOG);
    }

    public void show() {
        final Optional<ButtonType> buttonType = dialog.showAndWait();
        if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) logger.info("tapped the ok button");
        else {
            logger.info("tapped the copy and close button");
            Clipboards.copy(getController().getContent());
        }
    }

    @Override
    protected void handleButtons() {
        super.handleButtons();
        Button b  = (Button) pane.lookupButton(ButtonType.CANCEL);
        b.setText(getString("copy_close"));
    }
}
