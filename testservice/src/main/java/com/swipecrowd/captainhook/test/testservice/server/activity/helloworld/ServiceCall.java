package com.swipecrowd.captainhook.test.testservice.server.activity.helloworld;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.rxjava3.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.rxjava3.ratelimiter.operator.RateLimiterOperator;
import io.reactivex.rxjava3.core.Observable;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
public class ServiceCall {
    @Autowired private final CircuitBreaker circuitBreaker;
    @Autowired private final RateLimiter rateLimiter;

    public <T> Observable<T> run(final Observable<T> serviceFunction) {
        final RateLimiterOperator<T> rateLimiterOperator = RateLimiterOperator.of(rateLimiter);
        final CircuitBreakerOperator<T> circuitBreakerOperator = CircuitBreakerOperator.of(circuitBreaker);
        return serviceFunction
                .compose(rateLimiterOperator)
                .compose(circuitBreakerOperator);
    }
}
