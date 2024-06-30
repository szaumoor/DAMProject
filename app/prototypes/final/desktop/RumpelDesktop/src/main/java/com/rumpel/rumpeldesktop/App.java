package com.rumpel.rumpeldesktop;

import com.rumpel.rumpeldesktop.fxutils.prefs.Preferences;
import com.rumpel.rumpeldesktop.mvc.views.utils.ViewUtils;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.Objects;

/**
 * Application class for JavaFX. It always starts in the login view.
 */
public final class App extends Application {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void start(final Stage stage) {
        Locale.setDefault(Locale.forLanguageTag(Preferences.instance.getLanguage()));
        logger.info("JavaFX infrastructure started");
        stage.getIcons().add(new Image(Objects.requireNonNull(App.class.getResourceAsStream("dollar-icon.png"))));
        stage.centerOnScreen();
        ViewUtils.showLoginView(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}