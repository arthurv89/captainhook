package com.arthurvlug.captainhook.exampleservice2.activity.ping;

import lombok.Builder;
import lombok.Getter;
import com.arthurvlug.captainhook.framework.server.Input;

@Builder
@Getter
public class PingInput extends Input {
    private final String x;
}
