package com.swipecrowd.captainhook.framework.server;

import com.swipecrowd.captainhook.framework.server.AbstractRequestContext;
import com.swipecrowd.captainhook.framework.server.DefaultRequestContext;
import lombok.extern.slf4j.Slf4j;
import com.swipecrowd.captainhook.framework.common.response.Output;
import com.swipecrowd.captainhook.framework.server.AbstractActivity;
import com.swipecrowd.captainhook.framework.server.Input;

@Slf4j
public abstract class SimpleActivity<I extends Input, O extends Output> extends AbstractActivity<I, O, DefaultRequestContext> {
    @Override
    protected DefaultRequestContext preActivity(final I input) {
        return new DefaultRequestContext();
    }

    @Override
    protected void postActivity(final I input, final O output, final DefaultRequestContext requestContext) {
        log.info("[{}] Handled request", requestContext.getRequestId());
    }
}
