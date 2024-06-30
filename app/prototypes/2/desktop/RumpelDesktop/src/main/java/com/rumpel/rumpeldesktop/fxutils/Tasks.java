package com.rumpel.rumpeldesktop.fxutils;

import com.rumpel.rumpeldesktop.db.AbstractDAO;
import com.szaumoor.rumple.db.utils.Procedure;
import javafx.application.Platform;
import javafx.concurrent.Task;

public enum Tasks {
    ;

    public static Task<Void> createTask(final Procedure function) {
        return new Task<>() {
            @Override
            protected Void call() {
                function.execute();
                return null;
            }
        };
    }

    public static void exit() {
        AbstractDAO.closeClient();
        Platform.exit();
    }
}
