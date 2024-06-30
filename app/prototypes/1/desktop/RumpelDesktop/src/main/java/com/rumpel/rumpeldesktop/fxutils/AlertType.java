package com.rumpel.rumpeldesktop.fxutils;

import javafx.scene.control.Alert;

public enum AlertType {
    ERR(Alert.AlertType.ERROR),
    INFO(Alert.AlertType.INFORMATION),
    CONFIRM(Alert.AlertType.CONFIRMATION),
    NONE(Alert.AlertType.NONE),
    WARN(Alert.AlertType.WARNING);

    private final Alert.AlertType type;

    AlertType(Alert.AlertType type) {
        this.type = type;
    }

    public Alert.AlertType get() {
        return type;
    }
}
