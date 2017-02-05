package com.arthurvlug._service.activity._endpoint;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import nl.arthurvlug.captainhook.framework.common.response.Output;

@Builder
@Value
@EqualsAndHashCode(callSuper = false)
public class _EndpointOutput extends Output {
    private String y;
}
