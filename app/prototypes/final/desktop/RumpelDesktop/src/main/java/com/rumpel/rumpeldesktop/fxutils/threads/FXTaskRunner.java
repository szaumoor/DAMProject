package com.rumpel.rumpeldesktop.fxutils.threads;

import com.rumpel.rumpeldesktop.fxutils.Tasks;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Utility to launch multiple tasks in background threads, while allowing
 * a post-completion callback to be executed in the UI thread.
 */
public final class FXTaskRunner {
    private final List<Task<Void>> tasks;
    private final Consumer<Boolean> backgroundFunction;
    private final ExecutorService execWorker;
    private final ExecutorService execMonitor;

    /**
     * Constructor for launching multiple tasks in background threads.
     *
     * @param tasks    The tasks to execute in the background
     * @param callback The post-completion callback
     */
    public FXTaskRunner(final List<Task<Void>> tasks, final Consumer<Boolean> callback) {
        this.tasks = tasks;
        this.backgroundFunction = callback;
        this.execWorker = Executors.newWorkStealingPool();
        this.execMonitor = Executors.newSingleThreadExecutor();
    }

    /**
     * Constructor for launching one tasks in a background thread.
     *
     * @param task     The task to execute in the background
     * @param callback The post-completion callback
     */
    public FXTaskRunner(final Task<Void> task, final Consumer<Boolean> callback) {
        this.tasks = List.of(task);
        this.backgroundFunction = callback;
        this.execWorker = Executors.newWorkStealingPool();
        this.execMonitor = Executors.newSingleThreadExecutor();
    }

    /**
     * Executes the function with the given timeout and time unit.
     *
     * @param timeout  the timeout value
     * @param timeUnit the time unit of the timeout value
     */
    public void execute(final int timeout, final TimeUnit timeUnit) {
        var monitor = Tasks.createTask(() -> {
            addAndMonitorTasks();
            executeInBackgroundFXThread(timeout, timeUnit);
        });
        execMonitor.submit(monitor);
        execMonitor.shutdown();
    }

    /**
     * Private function to add the tasks and monitor them in the background.
     */
    private void addAndMonitorTasks() {
        for (final var task : tasks) execWorker.submit(task);
        execWorker.shutdown();
    }

    /**
     * Private function to handle the post-completion callback in the UI thread.
     *
     * @param timeout  the timeout value
     * @param timeUnit the time unit of the timeout value
     */
    private void executeInBackgroundFXThread(final int timeout, final TimeUnit timeUnit) {
        AtomicBoolean bool = new AtomicBoolean(false);
        try {
            bool.set(execWorker.awaitTermination(timeout, timeUnit));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Platform.runLater(() -> backgroundFunction.accept(bool.get()));
    }
}
