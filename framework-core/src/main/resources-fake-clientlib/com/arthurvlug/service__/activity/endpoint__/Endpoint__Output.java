package com.swipecrowd.service__.activity.endpoint__;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import com.swipecrowd.captainhook.framework.application.common.response.Output;

@Builder
@Value
@EqualsAndHashCode(callSuper = false)
public class Endpoint__Output extends Output {
    private String y;
}
