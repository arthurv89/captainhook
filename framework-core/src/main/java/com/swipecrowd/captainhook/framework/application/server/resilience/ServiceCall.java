package com.swipecrowd.captainhook.framework.application.server.resilience;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.rxjava3.bulkhead.operator.BulkheadOperator;
import io.github.resilience4j.rxjava3.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.rxjava3.ratelimiter.operator.RateLimiterOperator;
import io.github.resilience4j.rxjava3.retry.transformer.RetryTransformer;
import io.github.resilience4j.rxjava3.timelimiter.transformer.TimeLimiterTransformer;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.reactivex.rxjava3.core.Observable;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
public class ServiceCall {
    @Autowired private final CircuitBreaker circuitBreaker;
    @Autowired private final RateLimiter rateLimiter;
    @Autowired private final Bulkhead bulkhead;
    @Autowired private final Retry retry;
    @Autowired private final TimeLimiter timeLimiter;

    public <T> Observable<T> run(final Observable<T> serviceFunction) {
        return serviceFunction
                .compose(RateLimiterOperator.of(rateLimiter))
                .compose(CircuitBreakerOperator.of(circuitBreaker))
                .compose(BulkheadOperator.of(bulkhead))
                .compose(RetryTransformer.of(retry))
                .compose(TimeLimiterTransformer.of(timeLimiter));
    }
}
