package com.swipecrowd.captainhook.framework.server.resilience.config;

import lombok.Value;

@Value(staticConstructor = "of")
public class MaxConcurrentCalls {
    int value;
}
