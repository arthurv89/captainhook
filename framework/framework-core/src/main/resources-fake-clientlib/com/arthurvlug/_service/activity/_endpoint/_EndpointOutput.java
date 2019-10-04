package com.swipecrowd._service.activity._endpoint;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import com.swipecrowd.captainhook.framework.common.response.Output;

@Builder
@Value
@EqualsAndHashCode(callSuper = false)
public class _EndpointOutput extends Output {
    private String y;
}
