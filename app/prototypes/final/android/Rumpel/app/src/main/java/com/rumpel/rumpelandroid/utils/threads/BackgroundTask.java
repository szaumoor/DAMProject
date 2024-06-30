package com.rumpel.rumpelandroid.utils.threads;

import android.os.Handler;
import android.os.Looper;

/**
 * Class that encapsulates a background task and a post-execution callback
 */
public final class BackgroundTask implements Runnable{
    private final Runnable task;
    private final Runnable uiTask;
    private final Handler handler;

    /**
     * Constructor that takes two Runnables for the task and the post-execution callback
     *
     * @param task the task
     * @param uiTask the post-execution callback
     */
    public BackgroundTask(final Runnable task, final Runnable uiTask) {
        this.task = task;
        this.uiTask = uiTask;
        if (uiTask != null) {
            handler = new Handler(Looper.getMainLooper());
        } else {
            handler = null;
        }
    }

    /**
     * Constructor that takes a Runnable for the task and assumes no post-execution callback.
     *
     * @param task the task
     */
    public BackgroundTask(final Runnable task) {
        this.task = task;
        uiTask = null;
        handler = null;
    }

    /**
     * Run method of this BackgroundTask (which is a Runnable)
     */
    @Override
    public void run() {
        task.run();
        if (uiTask != null) handler.post(uiTask);
    }
}
