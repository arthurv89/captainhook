package com.swipecrowd.captainhook.framework.application.server;

import com.swipecrowd.captainhook.framework.application.common.AbstractActivity;
import com.swipecrowd.captainhook.framework.application.common.DefaultRequestContext;
import com.swipecrowd.captainhook.framework.application.common.Input;
import com.swipecrowd.captainhook.framework.application.common.Output;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class SimpleActivity<I extends Input, O extends Output> extends AbstractActivity<I, O, DefaultRequestContext> {
    @Override
    public DefaultRequestContext preActivity(final I input) {
        return new DefaultRequestContext();
    }

    @Override
    public void postActivity(final I input, final O output, final DefaultRequestContext requestContext) {
        log.info("[{}] Handled request", requestContext.getRequestId());
    }
}
