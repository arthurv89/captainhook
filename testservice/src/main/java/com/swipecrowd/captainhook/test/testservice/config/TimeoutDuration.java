package com.swipecrowd.captainhook.test.testservice.config;

import lombok.Value;

import java.time.Duration;

@Value(staticConstructor = "of")
public class TimeoutDuration {
    Duration duration;
}
