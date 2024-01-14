package com.swipecrowd.captainhook.test.testservice.activity.helloworld;

import com.swipecrowd.captainhook.framework.application.common.Output;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Instant;

@Builder
@Value
@EqualsAndHashCode(callSuper = false)
public class HelloWorldOutput extends Output {
    String message;
    Instant respondingTime;
}

