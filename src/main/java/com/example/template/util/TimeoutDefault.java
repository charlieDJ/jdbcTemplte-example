package com.example.template.util;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class TimeoutDefault {

    public static <T> CompletableFuture<T> with(T t, int ms) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
            }
            return t;
        });
    }

    public static <T> Supplier<T> with(Supplier<T> supplier, T t, int ms) {
        return () -> CompletableFuture.supplyAsync(supplier)
                .applyToEither(TimeoutDefault.with(t, ms), i -> i).join();
    }
}
