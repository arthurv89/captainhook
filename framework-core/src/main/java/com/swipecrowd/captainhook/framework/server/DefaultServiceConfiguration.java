package com.swipecrowd.captainhook.framework.server;

import com.swipecrowd.captainhook.framework.server.resilience.ServiceCall;
import com.swipecrowd.captainhook.framework.server.resilience.config.CircuitBreakerIgnoreExceptions;
import com.swipecrowd.captainhook.framework.server.resilience.config.FailureRateThreshold;
import com.swipecrowd.captainhook.framework.server.resilience.config.FairCallHandlingStrategyEnabled;
import com.swipecrowd.captainhook.framework.server.resilience.config.LimitForPeriod;
import com.swipecrowd.captainhook.framework.server.resilience.config.LimitRefreshPeriod;
import com.swipecrowd.captainhook.framework.server.resilience.config.MaxAttempts;
import com.swipecrowd.captainhook.framework.server.resilience.config.MaxConcurrentCalls;
import com.swipecrowd.captainhook.framework.server.resilience.config.MaxWaitDuration;
import com.swipecrowd.captainhook.framework.server.resilience.config.MinimumNumberOfCalls;
import com.swipecrowd.captainhook.framework.server.resilience.config.PermittedNumberOfCallsInHalfOpenState;
import com.swipecrowd.captainhook.framework.server.resilience.config.RecordExceptions;
import com.swipecrowd.captainhook.framework.server.resilience.config.RetryExceptions;
import com.swipecrowd.captainhook.framework.server.resilience.config.RetryIgnoreExceptions;
import com.swipecrowd.captainhook.framework.server.resilience.config.RetryOnException;
import com.swipecrowd.captainhook.framework.server.resilience.config.RetryOnResult;
import com.swipecrowd.captainhook.framework.server.resilience.config.SlidingWindowSize;
import com.swipecrowd.captainhook.framework.server.resilience.config.SlidingWindowTypeWrapper;
import com.swipecrowd.captainhook.framework.server.resilience.config.TimeoutDuration;
import com.swipecrowd.captainhook.framework.server.resilience.config.WaitDuration;
import com.swipecrowd.captainhook.framework.server.resilience.config.WaitDurationInOpenStateMs;
import com.swipecrowd.captainhook.framework.server.resilience.config.WritableStackTraceEnabled;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.core.EventPublisher;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

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
    public MaxAttempts createMaxAttempts(final AbstractServerProperties abstractServerProperties) {
        return MaxAttempts.of(abstractServerProperties.getMaxAttempts());
    }

    @Bean
    public WaitDuration createWaitDuration(final AbstractServerProperties abstractServerProperties) {
        return WaitDuration.of(abstractServerProperties.getWaitDuration());
    }

    @Bean
    public RetryOnResult createRetryOnResult() {
        return RetryOnResult.of(Optional.of(x -> false));
    }

    @Bean
    public RetryOnException createRetryOnException() {
        return RetryOnException.of(Optional.of(e -> false));
    }

    @Bean
    public RetryExceptions createRetryExceptions(final AbstractServerProperties abstractServerProperties) {
        return RetryExceptions.of(abstractServerProperties.getRetryExceptions());
    }

    @Bean
    public RetryIgnoreExceptions createRetryIgnoreExceptions(final AbstractServerProperties abstractServerProperties) {
        return RetryIgnoreExceptions.of(abstractServerProperties.getRetryIgnoreExceptions());
    }

    @Bean
    public Retry createRetry(final MaxAttempts maxAttempts,
                             final WaitDuration waitDuration,
                             final RetryOnResult retryOnResult,
                             final RetryOnException retryOnException,
                             final RetryExceptions retryExceptions,
                             final RetryIgnoreExceptions retryIgnoreExceptions) {
        RetryConfig.Builder<Object> builder = RetryConfig.custom();
        if(maxAttempts.getValue().isPresent()) {
            builder.maxAttempts(maxAttempts.getValue().get());
        }
        if(waitDuration.getValue().isPresent()) {
            builder.waitDuration(waitDuration.getValue().get());
        }
        if(retryOnResult.getValue().isPresent()) {
            builder.retryOnResult(retryOnResult.getValue().get());
        }
        if(retryOnException.getValue().isPresent()) {
            builder.retryOnException(retryOnException.getValue().get());
        }
        if(retryIgnoreExceptions.getValue().isPresent()) {
            builder.ignoreExceptions(retryIgnoreExceptions.getValue().get());
        }
        if(retryExceptions.getValue().isPresent()) {
            builder.retryExceptions(retryExceptions.getValue().get());
        }
        return Retry.of(DEFAULT, builder.build());
    }
    
    @Bean
    CircuitBreakerIgnoreExceptions createCircuitBreakerIgnoreExceptions(final AbstractServerProperties abstractServerProperties) {
        return CircuitBreakerIgnoreExceptions.of(abstractServerProperties.getCircuitBreakerIgnoreExceptions());
    }

    @Bean
    RecordExceptions createRecordExceptions(final AbstractServerProperties abstractServerProperties) {
        return RecordExceptions.of(abstractServerProperties.getRecordExceptions());
    }

    @Bean
    MinimumNumberOfCalls createMinimumNumberOfCalls(final AbstractServerProperties abstractServerProperties) {
        return MinimumNumberOfCalls.of(abstractServerProperties.getMinimumNumberOfCalls());
    }

    @Bean
    SlidingWindowSize createSlidingWindowSize(final AbstractServerProperties abstractServerProperties) {
        return SlidingWindowSize.of(abstractServerProperties.getSlidingWindowSize());
    }

    @Bean
    SlidingWindowTypeWrapper createSlidingWindowType(final AbstractServerProperties abstractServerProperties) {
        return SlidingWindowTypeWrapper.of(abstractServerProperties.getSlidingWindowType());
    }

    @Bean
    PermittedNumberOfCallsInHalfOpenState createPermittedNumberOfCallsInHalfOpenState(final AbstractServerProperties abstractServerProperties) {
        return PermittedNumberOfCallsInHalfOpenState.of(abstractServerProperties.getPermittedNumberOfCallsInHalfOpenState());
    }

    @Bean
    WaitDurationInOpenStateMs createWaitDurationInOpenStateMs(final AbstractServerProperties abstractServerProperties) {
        return WaitDurationInOpenStateMs.of(abstractServerProperties.getWaitDurationInOpenState());
    }

    @Bean
    FailureRateThreshold createFailureRateThreshold(final AbstractServerProperties abstractServerProperties) {
        return FailureRateThreshold.of(abstractServerProperties.getFailureRateThreshold());
    }

    @Bean
    public CircuitBreaker createCircuitBreaker(final CircuitBreakerIgnoreExceptions circuitBreakerIgnoreExceptions,
                                               final RecordExceptions recordExceptions,
                                               final MinimumNumberOfCalls minimumNumberOfCalls,
                                               final SlidingWindowSize slidingWindowSize,
                                               final SlidingWindowTypeWrapper slidingWindowType,
                                               final PermittedNumberOfCallsInHalfOpenState permittedNumberOfCallsInHalfOpenState,
                                               final WaitDurationInOpenStateMs waitDurationInOpenStateMs,
                                               final FailureRateThreshold failureRateThreshold) {
        CircuitBreakerConfig.Builder builder = CircuitBreakerConfig.custom();
        if(failureRateThreshold.getValue().isPresent()) {
            builder.failureRateThreshold(failureRateThreshold.getValue().get());
        }
        if(waitDurationInOpenStateMs.getValue().isPresent()) {
            builder.waitDurationInOpenState(waitDurationInOpenStateMs.getValue().get());
        }
        if(permittedNumberOfCallsInHalfOpenState.getValue().isPresent()) {
            builder.permittedNumberOfCallsInHalfOpenState(permittedNumberOfCallsInHalfOpenState.getValue().get());
        }
        if(slidingWindowSize.getValue().isPresent() || slidingWindowType.getValue().isPresent() || minimumNumberOfCalls.getValue().isPresent()) {
            builder.slidingWindow(
                    slidingWindowSize.getValue().get(),
                    minimumNumberOfCalls.getValue().get(),
                    slidingWindowType.getValue().get());
        }
        if(recordExceptions.getValue().isPresent()) {
            builder.recordExceptions(recordExceptions.getValue().get());
        }
        if(circuitBreakerIgnoreExceptions.getValue().isPresent()) {
            builder.ignoreExceptions(circuitBreakerIgnoreExceptions.getValue().get());
        }
        return CircuitBreaker.of(DEFAULT, builder.build());
    }

    @Bean
    public EventPublisher<?> createEventHandler(final CircuitBreaker circuitBreaker) {
        return circuitBreaker.getEventPublisher()
                .onError(event -> System.out.println(event));
    }

    @Bean
    TimeoutDuration createTimeoutDuration(final AbstractServerProperties abstractServerProperties) {
        return TimeoutDuration.of(abstractServerProperties.getTimeDuration());
    }

    @Bean
    LimitRefreshPeriod createLimitRefreshPeriod(final AbstractServerProperties abstractServerProperties) {
        return LimitRefreshPeriod.of(abstractServerProperties.getLimitRefreshPeriod());
    }

    @Bean
    LimitForPeriod createLimitForPeriod(final AbstractServerProperties abstractServerProperties) {
        return LimitForPeriod.of(abstractServerProperties.getLimitForPeriod());
    }

    @Bean
    public RateLimiter createRateLimiter(final TimeoutDuration timeoutDuration,
                                         final LimitRefreshPeriod limitRefreshPeriod,
                                         final LimitForPeriod limitForPeriod) {
        RateLimiterConfig.Builder builder = RateLimiterConfig.custom();
        if(timeoutDuration.getValue().isPresent()) {
            builder.timeoutDuration(timeoutDuration.getValue().get());
        }
        if(limitRefreshPeriod.getValue().isPresent()) {
            builder.limitRefreshPeriod(limitRefreshPeriod.getValue().get());
        }
        if(limitForPeriod.getValue().isPresent()) {
            builder.limitForPeriod(limitForPeriod.getValue().get());
        }
        return RateLimiter.of(DEFAULT, builder.build());
    }

    @Bean
    public FairCallHandlingStrategyEnabled createFairCallHandlingStrategyEnabled(final AbstractServerProperties abstractServerProperties) {
        return FairCallHandlingStrategyEnabled.of(abstractServerProperties.getFairCallHandlingStrategyEnabled());
    }

    @Bean
    public MaxConcurrentCalls maxConcurrentCalls(final AbstractServerProperties abstractServerProperties) {
        return MaxConcurrentCalls.of(abstractServerProperties.getMaxConcurrentCalls());
    }

    @Bean
    public MaxWaitDuration maxWaitDuration(final AbstractServerProperties abstractServerProperties) {
        return MaxWaitDuration.of(abstractServerProperties.getMaxWaitDuration());
    }

    @Bean
    public WritableStackTraceEnabled writableStackTraceEnabled(final AbstractServerProperties abstractServerProperties) {
        return WritableStackTraceEnabled.of(abstractServerProperties.getWritableStackTraceEnabled());
    }

    @Bean
    public Bulkhead createBulkhead(final FairCallHandlingStrategyEnabled fairCallHandlingStrategyEnabled,
                                   final MaxConcurrentCalls maxConcurrentCalls,
                                   final MaxWaitDuration maxWaitDuration,
                                   final WritableStackTraceEnabled writableStackTraceEnabled) {
        BulkheadConfig.Builder builder = BulkheadConfig.custom();
        if(fairCallHandlingStrategyEnabled.getValue().isPresent()) {
            builder.fairCallHandlingStrategyEnabled(fairCallHandlingStrategyEnabled.getValue().get());
        }
        if(maxConcurrentCalls.getValue().isPresent()) {
            builder.maxConcurrentCalls(maxConcurrentCalls.getValue().get());
        }
        if(maxWaitDuration.getValue().isPresent()) {
            builder.maxWaitDuration(maxWaitDuration.getValue().get());
        }
        if(writableStackTraceEnabled.getValue().isPresent()) {
            builder.writableStackTraceEnabled(writableStackTraceEnabled.getValue().get());
        }
        return Bulkhead.of(DEFAULT, builder.build());
    }
}
