package com.rumpel.rumpeldesktop.mvc.views;

import com.rumpel.rumpeldesktop.mvc.controllers.TagsDialogController;
import com.rumpel.rumpeldesktop.mvc.views.utils.ResourcePaths;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.util.Optional;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;

public final class TagDialog extends AbstractDialog<TagsDialogController> {

    public TagDialog(final Window owner) {
        super(owner, getString("tags"), ResourcePaths.TAG_DIALOG);
    }


    public void show() {
        final Optional<ButtonType> buttonType = dialog.showAndWait();
        if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK))
            logger.info("tapped the ok button");
        else {

        }
    }
}
