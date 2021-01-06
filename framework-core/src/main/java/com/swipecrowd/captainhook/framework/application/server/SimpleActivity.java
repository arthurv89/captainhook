package com.swipecrowd.captainhook.framework.application.server;

import com.swipecrowd.captainhook.framework.application.common.DefaultRequestContext;
import com.swipecrowd.captainhook.framework.application.common.Input;
import com.swipecrowd.captainhook.framework.application.common.response.Output;
import lombok.extern.slf4j.Slf4j;

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
