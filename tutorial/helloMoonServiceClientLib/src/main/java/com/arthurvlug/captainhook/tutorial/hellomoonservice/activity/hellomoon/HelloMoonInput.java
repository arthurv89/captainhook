package com.arthurvlug.captainhook.tutorial.hellomoonservice.activity.hellomoon;

import lombok.Builder;
import lombok.Getter;
import com.arthurvlug.captainhook.framework.server.Input;

@Builder
@Getter
public class HelloMoonInput extends Input {
    private final String name;
}
