package com.rumpel.rumpelandroid.utils.threads;

import java.util.concurrent.Callable;

public class RunningTask<T,R> implements Callable<R> {
    private T input;

    public RunningTask(T input) {
        this.input = input;
    }

    @Override
    public R call() throws Exception {
        return null;
    }
}
