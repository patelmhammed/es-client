package com.meesho.msearch.es.util;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
public class EsRetryUtils {

    private static final ScheduledExecutorService SCHEDULER =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "es-retry-scheduler");
                t.setDaemon(true);
                return t;
            });

    /**
     * Retry a CompletableFuture-returning operation with exponential backoff and jitter.
     * Mirrors the behavior of the former reactor Retry.backoff().jitter().
     *
     * @param futureSupplier supplier that performs the async operation
     * @param maxAttempts     maximum number of attempts (including initial)
     * @param baseDelay      base delay between retries
     * @param jitterFactor   jitter factor (0.0-1.0) for randomizing delay
     * @param retryLocation  label for logging
     * @return CompletableFuture that completes with the result or fails after exhausting retries
     */
    public static <T> CompletableFuture<T> retryFuture(
            Supplier<CompletableFuture<T>> futureSupplier,
            int maxAttempts,
            Duration baseDelay,
            double jitterFactor,
            String retryLocation) {
        CompletableFuture<T> result = new CompletableFuture<>();
        retryRecursive(futureSupplier, maxAttempts, baseDelay, jitterFactor, retryLocation, 0, result);
        return result;
    }

    private static <T> void retryRecursive(
            Supplier<CompletableFuture<T>> futureSupplier,
            int maxAttempts,
            Duration baseDelay,
            double jitterFactor,
            String retryLocation,
            int attempt,
            CompletableFuture<T> result) {
        futureSupplier.get()
                .whenComplete((value, ex) -> {
                    if (ex != null) {
                        if (attempt + 1 >= maxAttempts) {
                            log.error("Retry attempts exhausted after {} attempts at {}", maxAttempts, retryLocation);
                            result.completeExceptionally(ex);
                        } else {
                            log.warn("Retrying for location: {}, attempt {}/{}", retryLocation, attempt + 1, maxAttempts);
                            long delayMs = computeDelayMs(baseDelay.toMillis(), jitterFactor, attempt);
                            SCHEDULER.schedule(
                                    () -> retryRecursive(futureSupplier, maxAttempts, baseDelay, jitterFactor,
                                            retryLocation, attempt + 1, result),
                                    delayMs, TimeUnit.MILLISECONDS);
                        }
                    } else {
                        result.complete(value);
                    }
                });
    }

    private static long computeDelayMs(long baseMs, double jitterFactor, int attempt) {
        long delay = baseMs * (attempt + 1); // backoff: 1x, 2x, 3x, ...
        double jitter = 1.0 + (Math.random() * 2 - 1) * jitterFactor;
        return (long) (delay * jitter);
    }
}
