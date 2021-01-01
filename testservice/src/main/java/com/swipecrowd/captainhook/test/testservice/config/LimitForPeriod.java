package com.swipecrowd.captainhook.test.testservice.config;

import lombok.Value;

@Value(staticConstructor = "of")
public class LimitForPeriod {
    int period;
}
