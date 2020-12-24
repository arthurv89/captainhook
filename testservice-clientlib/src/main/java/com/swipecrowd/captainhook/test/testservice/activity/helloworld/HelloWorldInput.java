package com.swipecrowd.captainhook.test.testservice.activity.helloworld;

import lombok.Builder;
import lombok.Getter;
import com.swipecrowd.captainhook.framework.server.Input;

@Builder
@Getter
public class HelloWorldInput extends Input {
    private final String name;
    private final int forward;
}

