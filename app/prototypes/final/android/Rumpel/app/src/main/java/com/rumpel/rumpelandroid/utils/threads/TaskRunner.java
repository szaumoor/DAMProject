package com.rumpel.rumpelandroid.utils.threads;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Utility class to launch a list of tasks in parallel and wait for them to finish.
 * The waiting part must be handled by the caller with another thread so as to not block the UI.
 */
public final class TaskRunner {
    /**
     * Private constructor to prevent instantiation
     */
    private TaskRunner() {
        throw new AssertionError("Utility class");
    }

    /**
     * Executes a list of tasks in parallel and waits for them to finish, using a work-stealing pool.
     *
     * @param tasks the list of tasks
     */
    public static void executeAsync(final List<BackgroundTask> tasks) {
        var executor = Executors.newWorkStealingPool();
        tasks.forEach(executor::execute);
        executor.shutdown();
        try {
            executor.awaitTermination(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Executes a background task in a single thread.
     *
     * @param task The task to execute
     */
    public static void executeAsync(final BackgroundTask task) {
        var executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
        executor.shutdown();
    }
}