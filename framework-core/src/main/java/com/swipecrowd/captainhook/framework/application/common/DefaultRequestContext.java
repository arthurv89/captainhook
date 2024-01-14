package com.swipecrowd.captainhook.framework.application.common;

import lombok.Getter;

import java.util.UUID;

@Getter
public class DefaultRequestContext extends AbstractRequestContext {
    private final String requestId = UUID.randomUUID().toString();
}
