package com.swipecrowd.captainhook.framework.server;

import com.swipecrowd.captainhook.framework.server.resilience.ServiceCall;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.core.EventPublisher;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.context.annotation.Bean;

public class DefaultServiceConfiguration {
    public static final String DEFAULT = "default";

    @Bean
    public ServiceCall createServiceCall(final RateLimiter rateLimiter,
                                         final CircuitBreaker circuitBreaker,
                                         final Bulkhead bulkhead,
                                         final Retry retry,
                                         final TimeLimiter timeLimiter) {
        return new ServiceCall(circuitBreaker, rateLimiter, bulkhead, retry, timeLimiter);
    }

    @Bean
    public Retry createRetry(final AbstractServerProperties abstractServerProperties) {
        RetryConfig.Builder<Object> builder = RetryConfig.custom();
        abstractServerProperties.getMaxAttempts().ifPresent(x -> { builder.maxAttempts(x);
        });
        abstractServerProperties.getWaitDuration().ifPresent(x -> builder.waitDuration(x));
//        abstractServerProperties.getretryOnResult.ifPresent(x ->
//            builder.retryOnResult(x)
//        );
//        abstractServerProperties.getretryOnException.ifPresent(x ->
//            builder.retryOnException(e -> )
//        );
        abstractServerProperties.getRetryIgnoreExceptions().ifPresent(x -> builder.ignoreExceptions(x));
        abstractServerProperties.getRetryExceptions().ifPresent(x -> builder.retryExceptions(x));
        return Retry.of(DEFAULT, builder.build());
    }

    @Bean
    public CircuitBreaker createCircuitBreaker(final AbstractServerProperties abstractServerProperties) {
        CircuitBreakerConfig.Builder builder = CircuitBreakerConfig.custom();
        abstractServerProperties.getFailureRateThreshold().ifPresent(x -> builder.failureRateThreshold(x));
        abstractServerProperties.getWaitDurationInOpenState().ifPresent(x -> builder.waitDurationInOpenState(x));
        abstractServerProperties.getPermittedNumberOfCallsInHalfOpenState().ifPresent(x -> builder.permittedNumberOfCallsInHalfOpenState(x));
        abstractServerProperties.getSlidingWindowSize().ifPresent(size ->
                abstractServerProperties.getSlidingWindowType().ifPresent(type ->
                        abstractServerProperties.getMinimumNumberOfCalls().ifPresent(calls ->
                                builder.slidingWindow(size, calls, type)
        )));
        abstractServerProperties.getRecordExceptions().ifPresent(x -> builder.recordExceptions(x));
        abstractServerProperties.getCircuitBreakerIgnoreExceptions().ifPresent(x -> builder.ignoreExceptions(x));
        return CircuitBreaker.of(DEFAULT, builder.build());
    }

    @Bean
    public RateLimiter createRateLimiter(final AbstractServerProperties abstractServerProperties) {
        RateLimiterConfig.Builder builder = RateLimiterConfig.custom();
        abstractServerProperties.getTimeDuration().ifPresent(x -> builder.timeoutDuration(x));
        abstractServerProperties.getLimitRefreshPeriod().ifPresent(x -> builder.limitRefreshPeriod(x));
        abstractServerProperties.getLimitForPeriod().ifPresent(x -> builder.limitForPeriod(x));
        return RateLimiter.of(DEFAULT, builder.build());
    }

    @Bean
    public Bulkhead createBulkhead(final AbstractServerProperties abstractServerProperties) {
        BulkheadConfig.Builder builder = BulkheadConfig.custom();
        abstractServerProperties.getFairCallHandlingStrategyEnabled().ifPresent(x -> builder.fairCallHandlingStrategyEnabled(x));
        abstractServerProperties.getMaxConcurrentCalls().ifPresent(x -> builder.maxConcurrentCalls(x));
        abstractServerProperties.getMaxWaitDuration().ifPresent(x -> builder.maxWaitDuration(x));
        abstractServerProperties.getWritableStackTraceEnabled().ifPresent(x -> builder.writableStackTraceEnabled(x));
        return Bulkhead.of(DEFAULT, builder.build());
    }

    @Bean
    public TimeLimiter timeLimiter(final AbstractServerProperties abstractServerProperties) {
        TimeLimiterConfig.Builder builder = TimeLimiterConfig.custom();
        abstractServerProperties.getCancelRunningFuture().ifPresent(x -> builder.cancelRunningFuture(x));
        abstractServerProperties.getTimeoutDuration().ifPresent(x -> builder.timeoutDuration(x));
        return TimeLimiter.of(DEFAULT, builder.build());
    }

    @Bean
    public EventPublisher<?> createEventHandler(final CircuitBreaker circuitBreaker) {
        return circuitBreaker.getEventPublisher() .onError(event -> System.out.println(event));
    }

}
