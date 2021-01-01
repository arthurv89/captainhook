package com.swipecrowd.captainhook.framework.server.resilience.config;

import lombok.Value;

import java.util.Optional;

@Value(staticConstructor = "of")
public class MinimumNumberOfCalls {
    Optional<Integer> value;
}
