package com.swipecrowd.captainhook.framework.server.resilience.config;

import lombok.Value;

import java.util.Optional;
import java.util.function.Predicate;

@Value(staticConstructor = "of")
public class RetryOnException {
    Optional<Predicate<Throwable>> value;
}