package com.arthurvlug._service.activity._endpoint;

import lombok.Builder;
import lombok.Getter;
import nl.arthurvlug.captainhook.framework.server.Input;

@Builder
@Getter
public class _EndpointInput extends Input {
    private final String x;
}
