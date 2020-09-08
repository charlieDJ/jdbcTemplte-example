package com.example.template.util;

import java.util.concurrent.*;
import java.util.function.Supplier;

public class FutureUtil {

    private static final ScheduledExecutorService schedulerExecutor = Executors.newScheduledThreadPool(10);
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public static <T> CompletableFuture<T> supplyAsync( final Supplier<T> supplier, long timeoutValue,
                                                        TimeUnit timeUnit, T defaultValue) {

        final CompletableFuture<T> cf = new CompletableFuture<T>();

        // as pointed out by Peti, the ForkJoinPool.commonPool() delivers a
        // ForkJoinTask implementation of Future, that doesn't interrupt when cancelling
        // Using Executors.newCachedThreadPool instead in the example
        // submit task
        Future<?> future = executorService.submit(() -> {
            try {
                cf.complete(supplier.get());
            } catch (Throwable ex) {
                cf.completeExceptionally(ex);
            }
        });

        //schedule watcher
        schedulerExecutor.schedule(() -> {
            if (!cf.isDone()) {
                cf.complete(defaultValue);
                future.cancel(true);
            }

        }, timeoutValue, timeUnit);

        return cf;
    }
}
