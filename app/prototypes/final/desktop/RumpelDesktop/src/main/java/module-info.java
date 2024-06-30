module RumpelDesktop {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.swing;
    requires java.desktop;
    requires java.prefs;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires org.apache.logging.log4j;
    requires org.kordamp.bootstrapfx.core;
    requires RumpelModel;
    requires JavaUtils;
    requires org.apache.pdfbox;
    requires org.controlsfx.controls;

    exports com.rumpel.rumpeldesktop to javafx.graphics;
    exports com.rumpel.rumpeldesktop.mvc.controllers to javafx.fxml;
    exports com.rumpel.rumpeldesktop.mvc.views.utils;
    exports com.rumpel.rumpeldesktop.fxutils;
    opens com.rumpel.rumpeldesktop.mvc.controllers to javafx.fxml;
    exports com.rumpel.rumpeldesktop.mvc.controllers.injectors to javafx.fxml;
    opens com.rumpel.rumpeldesktop.mvc.controllers.injectors to javafx.fxml;
    exports com.rumpel.rumpeldesktop.fxutils.prefs;
    exports com.rumpel.rumpeldesktop.mvc.views;
    exports com.rumpel.rumpeldesktop.fxutils.threads;
    opens com.rumpel.rumpeldesktop.fxutils to javafx.fxml;
    opens com.rumpel.rumpeldesktop.mvc.views.utils to javafx.fxml;
}