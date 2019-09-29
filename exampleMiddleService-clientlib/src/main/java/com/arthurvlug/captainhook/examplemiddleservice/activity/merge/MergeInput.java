package com.arthurvlug.captainhook.examplemiddleservice.activity.merge;

import lombok.Builder;
import lombok.Getter;
import nl.arthurvlug.captainhook.framework.server.Input;

@Builder
@Getter
public class MergeInput extends Input {
    private final String name;
}
