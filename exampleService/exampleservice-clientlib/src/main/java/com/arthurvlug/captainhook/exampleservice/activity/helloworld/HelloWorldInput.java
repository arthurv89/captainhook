package com.arthurvlug.captainhook.exampleservice.activity.helloworld;

import lombok.Builder;
import lombok.Getter;
import com.arthurvlug.captainhook.framework.server.Input;

@Builder
@Getter
public class HelloWorldInput extends Input {
    private final String name;
}
