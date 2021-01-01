package com.swipecrowd.captainhook.framework.server.resilience.config;

import lombok.Value;

import java.util.function.Predicate;

@Value(staticConstructor = "of")
public class RetryOnException {
    Predicate<Throwable> value;
}
