package com.rumpel.rumpeldesktop.fxutils;

import javafx.concurrent.Task;

public final class Tasks {

    public static Task<Void> createTask(final VoidNoArgsFunction function) {
        return new Task<>() {
            @Override
            protected Void call() {
                function.execute();
                return null;
            }
        };
    }
}
