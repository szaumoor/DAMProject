package com.rumpel.rumpeldesktop.mvc.views.utils;

import com.rumpel.rumpeldesktop.App;
import com.rumpel.rumpeldesktop.fxutils.Translator;
import com.rumpel.rumpeldesktop.fxutils.Dimensions;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

import static com.rumpel.rumpeldesktop.fxutils.Translator.getString;

/**
 * Utility class to switch the current scene of the application
 */
public final class ViewUtils {
    /**
     * Private constructor to prevent instantiation
     */
    private ViewUtils() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    private static final Logger logger = LogManager.getLogger();

    public static void showLoginView(final Stage stage) {
        final FXMLLoader loginLoader = new FXMLLoader(App.class.getResource(ResourcePaths.LOGIN), Translator.getBundle());
        final Scene loginScene;
        try {
            loginScene = new Scene(loginLoader.load(), Dimensions.CREDENTIALS_DIMENSIONS.width().value(), Dimensions.CREDENTIALS_DIMENSIONS.height().value());
        } catch (IOException e) {
            logger.error(e);
            return;
        }
        loginScene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setMinHeight(Dimensions.CREDENTIALS_DIMENSIONS.height().value());
        stage.setMinWidth(Dimensions.CREDENTIALS_DIMENSIONS.width().value());
        stage.setTitle(getString("rumpel_welcome"));
        stage.setScene(loginScene);
        stage.setResizable(false);
        stage.show();
    }

    public static void switchScene(final Stage stage, final String fxml, final String title, final Dimension dimension, final boolean resizable) {
        Parent root;
        try {
            root = FXMLLoader.load(App.class.getResource(fxml), Translator.getBundle());
        } catch (IOException ex) {
            logger.error(ex.fillInStackTrace());
            return;
        }
        stage.setTitle(title);
        Scene scene = new Scene(root, dimension.width().value(), dimension.height().value());
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setResizable(resizable);
        stage.setScene(scene);
        stage.setMinWidth(dimension.width().value());
        stage.setMinHeight(dimension.height().value());
        stage.show();
    }
}
