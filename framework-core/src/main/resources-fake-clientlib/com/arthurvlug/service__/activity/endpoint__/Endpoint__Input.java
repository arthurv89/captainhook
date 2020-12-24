package com.swipecrowd.service__.activity.endpoint__;

import lombok.Builder;
import lombok.Getter;
import com.swipecrowd.captainhook.framework.server.Input;

@Builder
@Getter
public class Endpoint__Input extends Input {
    private final String x;
}
