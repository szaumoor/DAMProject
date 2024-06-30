package com.rumpel.rumpeldesktop.fxutils;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public final class FXTaskRunner {

    private final List<Task<Void>> tasks;
    private final Consumer<Boolean> backgroundFunction;
    private final ExecutorService execWorker;
    private final ExecutorService execMonitor;

    public FXTaskRunner(final List<Task<Void>> tasks, final Consumer<Boolean> backgroundFunction) {
        this.tasks = tasks;
        this.backgroundFunction = backgroundFunction;
        this.execWorker =  Executors.newFixedThreadPool((int) Math.ceil(tasks.size() / 2.0));
        this.execMonitor = Executors.newSingleThreadExecutor();
    }

    public void execute(final int timeout, final TimeUnit timeUnit) {
        var monitor = Tasks.createTask(() -> {
            addAndMonitorTasks();
            executeInBackgroundFXThread(timeout, timeUnit);
        });
        execMonitor.submit(monitor);
        execMonitor.shutdown();
    }

    private void addAndMonitorTasks() {
        for (final var task : tasks) execWorker.submit(task);
        execWorker.shutdown();
    }

    private void executeInBackgroundFXThread(final int timeout, final TimeUnit timeUnit) {
        AtomicBoolean bool = new AtomicBoolean(false);
        try {
            bool.set(execWorker.awaitTermination(timeout, timeUnit));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Platform.runLater(()->backgroundFunction.accept(bool.get()));
    }
}
