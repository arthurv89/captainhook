package com.swipecrowd.captainhook.framework.server.resilience.config;

import lombok.Value;

import java.util.Optional;

@Value(staticConstructor = "of")
public class RetryIgnoreExceptions {
    Optional<Class<? extends Throwable>[]> value;
}
