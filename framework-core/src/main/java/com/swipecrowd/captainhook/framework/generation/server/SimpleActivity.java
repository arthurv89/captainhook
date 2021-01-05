package com.swipecrowd.captainhook.framework.server;

import com.swipecrowd.captainhook.framework.common.response.Output;
import com.swipecrowd.captainhook.framework.generation.AbstractActivity;
import com.swipecrowd.captainhook.framework.generation.DefaultRequestContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class SimpleActivity<I extends Input, O extends Output> extends AbstractActivity<I, O, DefaultRequestContext> {
    @Override
    protected com.swipecrowd.captainhook.framework.generation.DefaultRequestContext preActivity(final I input) {
        return new DefaultRequestContext();
    }

    @Override
    protected void postActivity(final I input, final O output, final DefaultRequestContext requestContext) {
        log.info("[{}] Handled request", requestContext.getRequestId());
    }
}
