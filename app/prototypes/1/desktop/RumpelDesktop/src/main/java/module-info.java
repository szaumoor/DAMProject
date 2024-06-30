module RumpelDesktop {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires org.apache.logging.log4j;
    requires org.kordamp.bootstrapfx.core;
    requires RumpelModel;

    exports com.rumpel.rumpeldesktop to javafx.graphics;
    exports com.rumpel.rumpeldesktop.mvc.controllers to javafx.fxml;
    exports com.rumpel.rumpeldesktop.mvc.views.utils;
    exports com.rumpel.rumpeldesktop.fxutils;
    opens com.rumpel.rumpeldesktop.mvc.controllers to javafx.fxml;
}