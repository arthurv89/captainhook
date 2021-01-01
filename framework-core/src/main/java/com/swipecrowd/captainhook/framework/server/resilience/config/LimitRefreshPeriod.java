package com.swipecrowd.captainhook.framework.server.resilience.config;

import lombok.Value;

import java.time.Duration;
import java.util.Optional;

@Value(staticConstructor = "of")
public class LimitRefreshPeriod {
    Optional<Duration> value;
}
