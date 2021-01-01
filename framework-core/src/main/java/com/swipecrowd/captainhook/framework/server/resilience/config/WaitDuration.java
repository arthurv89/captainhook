package com.swipecrowd.captainhook.framework.server.resilience.config;

import lombok.Value;

import java.time.Duration;

@Value(staticConstructor = "of")
public class WaitDuration {
    Duration value;
}
