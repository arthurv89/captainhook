package com.swipecrowd.captainhook.framework.application.common;

import lombok.Value;

@Value
public class Request<I extends Input> {
    private final I input;
}
