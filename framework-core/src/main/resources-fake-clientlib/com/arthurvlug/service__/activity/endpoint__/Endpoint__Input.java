package com.swipecrowd.service__.activity.endpoint__;

import lombok.Builder;
import lombok.Getter;
import com.swipecrowd.captainhook.framework.application.server.Input;

@Builder
@Getter
public class Endpoint__Input extends Input {
    private final String x;
}
