package com.swipecrowd.captainhook.framework.server.resilience.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import lombok.Value;

import java.util.Optional;

@Value(staticConstructor = "of")
public class SlidingWindowTypeWrapper {
    Optional<SlidingWindowType> value;
}
