package com.example.template;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FutureTest {


    @Test
    public void simple() throws InterruptedException, ExecutionException {
        Future<String> completableFuture = calculateAsync();

        String result = completableFuture.get();
        assertEquals("Hello", result);
    }

    public Future<String> calculateAsync() throws InterruptedException {
        CompletableFuture<String> completableFuture
                = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(500);
            completableFuture.complete("Hello");
            return null;
        });

        return completableFuture;
    }

    @Test
    public void thenApplyTest() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> thenApply = completableFuture.thenApply(s -> s + " world");
        assertEquals("hello world", thenApply.get());
    }

    @Test
    public void composeTest() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> compose = completableFuture.thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " world"));
        assertEquals("hello world", compose.get());
    }

    @Test
    public void combineTest() throws ExecutionException, InterruptedException {
        CompletableFuture<String> combine = CompletableFuture.supplyAsync(() -> "hello")
                .thenCombine(CompletableFuture.supplyAsync(() -> " world"), (s1, s2) -> s1 + s2);
        assertEquals("hello world", combine.get());
    }

    @Test
    public void allOfTest() {
        CompletableFuture<String> future1
                = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> future2
                = CompletableFuture.supplyAsync(() -> "Beautiful");
        CompletableFuture<String> future3
                = CompletableFuture.supplyAsync(() -> "World");

        CompletableFuture<Void> combinedFuture
                = CompletableFuture.allOf(future1, future2, future3);

        String combined = Stream.of(future1, future2, future3)
                .map(CompletableFuture::join)
                .collect(Collectors.joining(" "));
        assertEquals("Hello Beautiful World", combined);
    }

    @Test
    public void handleError() throws ExecutionException, InterruptedException {
        String name = null;
        CompletableFuture<String> handle = CompletableFuture.supplyAsync(() -> {
            if (name == null) {
                throw new RuntimeException("Completion Error");
            }
            return "Hello, " + name;
        }).handle((s, t) -> s != null ? s : "Hello, Stranger!");
        assertEquals("Hello, Stranger!", handle.get());
    }

    @Test
    public void whenParametersToThreadWithLamda_thenParametersPassedCorrectly()
            throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        int[] numbers = new int[]{4, 5, 6};

        try {
            Future<Integer> sumResult =
                    executorService.submit(() -> IntStream.of(numbers).sum());
            Future<Double> averageResult =
                    executorService.submit(() -> IntStream.of(numbers).average().orElse(0d));
            assertEquals(Integer.valueOf(15), sumResult.get());
            assertEquals(Double.valueOf(5.0), averageResult.get());
        } finally {
            executorService.shutdown();
        }
    }

}
