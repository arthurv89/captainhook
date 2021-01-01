package com.swipecrowd.captainhook.framework.server;

import com.swipecrowd.captainhook.framework.server.resilience.ServiceCall;
import com.swipecrowd.captainhook.framework.server.resilience.config.FairCallHandlingStrategyEnabled;
import com.swipecrowd.captainhook.framework.server.resilience.config.IgnoreExceptions;
import com.swipecrowd.captainhook.framework.server.resilience.config.LimitForPeriod;
import com.swipecrowd.captainhook.framework.server.resilience.config.LimitRefreshPeriod;
import com.swipecrowd.captainhook.framework.server.resilience.config.MaxAttempts;
import com.swipecrowd.captainhook.framework.server.resilience.config.MaxConcurrentCalls;
import com.swipecrowd.captainhook.framework.server.resilience.config.MaxWaitDuration;
import com.swipecrowd.captainhook.framework.server.resilience.config.RetryExceptions;
import com.swipecrowd.captainhook.framework.server.resilience.config.RetryOnException;
import com.swipecrowd.captainhook.framework.server.resilience.config.RetryOnResult;
import com.swipecrowd.captainhook.framework.server.resilience.config.TimeoutDuration;
import com.swipecrowd.captainhook.framework.server.resilience.config.WaitDuration;
import com.swipecrowd.captainhook.framework.server.resilience.config.WritableStackTraceEnabled;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.core.EventPublisher;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

public class DefaultServiceConfiguration {
    private static final String DEFAULT = "default";

    @Bean
    public ServiceCall createServiceCall(final RateLimiter rateLimiter,
                                         final CircuitBreaker circuitBreaker,
                                         final Bulkhead bulkhead,
                                         final Retry retry) {
        return new ServiceCall(circuitBreaker, rateLimiter, bulkhead, retry);
    }

    @Bean
    public MaxAttempts createMaxAttempts(final AbstractServerProperties AbstractServerProperties) {
        return MaxAttempts.of(AbstractServerProperties.getMaxAttempts().orElse(1));
    }

    @Bean
    public WaitDuration createWaitDuration(final AbstractServerProperties AbstractServerProperties) {
        return WaitDuration.of(Duration.ofMillis(AbstractServerProperties.getWaitDurationMs().orElse(1)));
    }

    @Bean
    public RetryOnResult createRetryOnResult() {
        return RetryOnResult.of(x -> false);
    }

    @Bean
    public RetryOnException createRetryOnException() {
        return RetryOnException.of(e -> false);
    }

    @Bean
    public RetryExceptions createRetryExceptions() {
        return RetryExceptions.of(null);
    }

    @Bean
    public IgnoreExceptions createIgnoreExceptions() {
        return IgnoreExceptions.of(null);
    }

    @Bean
    public Retry createRetry(final MaxAttempts maxAttempts,
                             final WaitDuration waitDuration,
                             final RetryOnResult retryOnResult,
                             final RetryOnException retryOnException,
                             final RetryExceptions retryExceptions,
                             final IgnoreExceptions ignoreExceptions) {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(maxAttempts.getValue())
                .waitDuration(waitDuration.getValue())
                .retryOnResult(retryOnResult.getValue())
                .retryOnException(retryOnException.getValue())
                .ignoreExceptions(ignoreExceptions.getValue())
                .retryExceptions(retryExceptions.getValue())
                .build();

        // Create a RetryRegistry with a custom global configuration
        RetryRegistry registry = RetryRegistry.of(config);

        return registry.retry(DEFAULT, config);
    }

    @Bean
    public CircuitBreaker createCircuitBreaker() {
        return CircuitBreaker.ofDefaults(DEFAULT);
    }

    @Bean
    public EventPublisher createEventHandler(final CircuitBreaker circuitBreaker) {
        return circuitBreaker.getEventPublisher()
                .onError(event -> System.out.println(event));
    }

    @Bean
    TimeoutDuration createTimeoutDuration(final AbstractServerProperties abstractServerProperties) {
        return TimeoutDuration.of(Duration.ofSeconds(abstractServerProperties.getTimeDuration().orElse(1)));
    }

    @Bean
    LimitRefreshPeriod createLimitRefreshPeriod(final AbstractServerProperties abstractServerProperties) {
        return LimitRefreshPeriod.of(Duration.ofSeconds(abstractServerProperties.getLimitRefreshPeriod().orElse(1)));
    }

    @Bean
    LimitForPeriod createLimitForPeriod(final AbstractServerProperties abstractServerProperties) {
        return LimitForPeriod.of(abstractServerProperties.getLimitForPeriod().orElse(1));
    }

    @Bean
    public RateLimiter createRateLimiter(final TimeoutDuration timeoutDuration,
                                         final LimitRefreshPeriod limitRefreshPeriod,
                                         final LimitForPeriod limitForPeriod) {
        final RateLimiterConfig config = RateLimiterConfig.custom()
                .timeoutDuration(timeoutDuration.getValue())
                .limitRefreshPeriod(limitRefreshPeriod.getValue())
                .limitForPeriod(limitForPeriod.getValue())
                .build();

        return RateLimiter.of(DEFAULT, config);
    }

    @Bean
    public FairCallHandlingStrategyEnabled createFairCallHandlingStrategyEnabled(final AbstractServerProperties abstractServerProperties) {
        return FairCallHandlingStrategyEnabled.of(abstractServerProperties.getFairCallHandlingStrategyEnabled().orElse(false));
    }

    @Bean
    public MaxConcurrentCalls maxConcurrentCalls(final AbstractServerProperties AbstractServerProperties) {
        return MaxConcurrentCalls.of(AbstractServerProperties.getMaxConcurrentCalls().orElse(1));
    }

    @Bean
    public MaxWaitDuration maxWaitDuration(final AbstractServerProperties abstractServerProperties) {
        return MaxWaitDuration.of(Duration.ofSeconds(abstractServerProperties.getMaxWaitDuration().orElse(1)));
    }

    @Bean
    public WritableStackTraceEnabled writableStackTraceEnabled(final AbstractServerProperties AbstractServerProperties) {
        return WritableStackTraceEnabled.of(AbstractServerProperties.getWritableStackTraceEnabled().orElse(false));
    }

    @Bean
    public Bulkhead createBulkhead(final FairCallHandlingStrategyEnabled fairCallHandlingStrategyEnabled,
                                   final MaxConcurrentCalls maxConcurrentCalls,
                                   final MaxWaitDuration maxWaitDuration,
                                   final WritableStackTraceEnabled writableStackTraceEnabled) {
        final BulkheadConfig config = BulkheadConfig.custom()
                .fairCallHandlingStrategyEnabled(fairCallHandlingStrategyEnabled.isEnabled())
                .maxConcurrentCalls(maxConcurrentCalls.getValue())
                .maxWaitDuration(maxWaitDuration.getValue())
                .writableStackTraceEnabled(writableStackTraceEnabled.isEnabled())
                .build();
        return Bulkhead.of(DEFAULT, config);
    }
}
