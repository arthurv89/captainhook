package com.swipecrowd.captainhook.framework.application.server;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import lombok.Getter;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.stream.Stream;

import static com.swipecrowd.captainhook.framework.application.server.ApplicationArguments.PORT_KEY;
import static com.swipecrowd.captainhook.framework.application.server.PropertiesUtils.getOrThrow;

public abstract class AbstractServerProperties {
    @Getter private final String stage;
    @Getter private final String region;
    @Getter private final ApplicationArguments applicationArguments;
    @Getter private final int port;
    @Getter private final String name;

    @Getter private final Optional<Class<? extends Throwable>[]> retryExceptions;
    @Getter private final Optional<Integer> maxAttempts;
    @Getter private final Optional<Duration> waitDuration;
    @Getter private final Optional<Boolean> writableStackTraceEnabled;
    @Getter private final Optional<Integer> maxConcurrentCalls;
    @Getter private final Optional<Integer> limitForPeriod;
    @Getter private final Optional<Boolean> fairCallHandlingStrategyEnabled;
    @Getter private final Optional<Duration> maxWaitDuration;
    @Getter private final Optional<Duration> timeDuration;
    @Getter private final Optional<Duration> limitRefreshPeriod;
    @Getter private final Optional<Class<? extends Throwable>[]> ignoreExceptions;
    @Getter private final Optional<Float> failureRateThreshold;
    @Getter private final Optional<Duration> waitDurationInOpenState;
    @Getter private final Optional<Integer> permittedNumberOfCallsInHalfOpenState;
    @Getter private final Optional<SlidingWindowType> slidingWindowType;
    @Getter private final Optional<Integer> slidingWindowSize;
    @Getter private final Optional<Integer> minimumNumberOfCalls;
    @Getter private final Optional<Class<? extends Throwable>[]> recordExceptions;
    @Getter private final Optional<Class<? extends Throwable>[]> circuitBreakerIgnoreExceptions;
    @Getter private final Optional<Class<? extends Throwable>[]> retryIgnoreExceptions;
    @Getter private final Optional<Boolean> cancelRunningFuture;
    @Getter private final Optional<Duration> timeoutDuration;

    public AbstractServerProperties(final ApplicationArguments applicationArguments) {
        this.applicationArguments = applicationArguments;
        this.stage = getOrThrow("stage", applicationArguments);
        this.region = getOrThrow("region", applicationArguments);

        this.name = PropertiesUtils.get("name", stage, region, applicationArguments);
        this.port = Integer.parseInt(PropertiesUtils.get(PORT_KEY, stage, region, applicationArguments));

        this.retryExceptions = getExceptionClasses("*.*.retryExceptions");
        this.maxAttempts = getInt("*.*.maxAttempts");
        this.waitDuration = getDuration("*.*.waitDurationMs");
        this.writableStackTraceEnabled = getBool("*.*.writableStackTraceEnabled");
        this.maxConcurrentCalls = getInt("*.*.maxConcurrentCalls");
        this.limitForPeriod = getInt("*.*.limitForPeriod");
        this.fairCallHandlingStrategyEnabled = getBool("*.*.fairCallHandlingStrategyEnabled");
        this.maxWaitDuration = getDuration("*.*.maxWaitDuration");
        this.timeDuration = getDuration("*.*.timeDuration");
        this.limitRefreshPeriod = getDuration("*.*.limitRefreshPeriod");
        this.ignoreExceptions = getExceptionClasses("*.*.ignoreExceptions");

        this.failureRateThreshold = getFloat("*.*.failureRateThreshold");
        this.waitDurationInOpenState = getDuration("*.*.waitDurationInOpenStateMs");
        this.permittedNumberOfCallsInHalfOpenState = getInt("*.*.permittedNumberOfCallsInHalfOpenState");
        this.slidingWindowType = getEnum("*.*.slidingWindowType", SlidingWindowType.class);
        this.slidingWindowSize = getInt("*.*.slidingWindowSize");
        this.minimumNumberOfCalls = getInt("*.*.minimumNumberOfCalls");
        this.recordExceptions = getExceptionClasses("*.*.recordExceptions");
        this.circuitBreakerIgnoreExceptions = getExceptionClasses("*.*.circuitBreakerIgnoreExceptions");
        this.retryIgnoreExceptions = getExceptionClasses("*.*.retryIgnoreExceptions");
        this.cancelRunningFuture = getBool("*.*.cancelRunningFuture");
        this.timeoutDuration = getDuration("*.*.timeoutDuration");
    }

    private Optional<Duration> getDuration(final String key) {
        return getLong(key).map(ms -> Duration.ofMillis(ms));
    }

    private Optional<Class<? extends Throwable>[]> getExceptionClasses(final String key) {
        return applicationArguments.get(key)
                .map(x -> x.split(","))
                .map(classNames -> {
                    final Stream<String> classNamesStream = Arrays.stream(classNames).flatMap(Stream::of);
                    final IntFunction<Class<? extends Throwable>[]> aNew = Class[]::new;
                    return classNamesStream
                            .map(clazz -> toClass(clazz))
                            .toArray(aNew);
                });
    }

    private <T> Optional<T> getEnum(final String key, final Class<T> type) {
        return applicationArguments.get(key)
                .flatMap(x -> Arrays.stream(type.getEnumConstants()).filter(enumm -> enumm.equals(x)).findFirst());
    }

    private Optional<Long> getLong(final String key) {
        return applicationArguments.get(key).map(x -> Long.valueOf(x));
    }

    private Optional<Float> getFloat(final String key) {
        return applicationArguments.get(key).map(x -> Float.valueOf(x));
    }

    private Optional<Boolean> getBool(final String key) {
        return applicationArguments.get(key).map(x -> Boolean.valueOf(x));
    }

    private Optional<Integer> getInt(final String key) {
        return applicationArguments.get(key).map(x -> Integer.valueOf(x));
    }

    private Class<? extends Throwable> toClass(final String clazz) {
        try {
            return (Class<? extends Throwable>) Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
