package com.swipecrowd.captainhook.test.testservice.activity.helloworld;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.swipecrowd.captainhook.framework.server.Input;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class HelloWorldInput extends Input {
    @JsonCreator
    private HelloWorldInput() {}

    private String name;
    private int forward;
}

