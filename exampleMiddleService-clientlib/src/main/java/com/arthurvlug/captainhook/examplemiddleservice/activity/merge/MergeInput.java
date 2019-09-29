package com.arthurvlug.captainhook.examplemiddleservice.activity.merge;

import lombok.Builder;
import lombok.Getter;
import nl.arthurvlug.captainhook.framework.common.Input;

@Builder
@Getter
public class MergeInput extends Input {
    private final String name;
}
