package com.swipecrowd.captainhook.framework.application.common;

import com.swipecrowd.captainhook.framework.application.server.AbstractRequestContext;
import lombok.Getter;

import java.util.UUID;

@Getter
public class DefaultRequestContext extends AbstractRequestContext {
    private final String requestId = UUID.randomUUID().toString();
}
