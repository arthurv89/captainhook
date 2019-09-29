package com.arthurvlug.captainhook.examplemiddleservice.server.activity;

import lombok.extern.slf4j.Slf4j;
import nl.arthurvlug.captainhook.framework.common.Input;
import nl.arthurvlug.captainhook.framework.common.response.Output;
import nl.arthurvlug.captainhook.framework.server.AbstractActivity;

@Slf4j
public abstract class AbstractExampleActivity<I extends Input, O extends Output>
        extends AbstractActivity<I, O, ExampleServiceRequestContext> {
    @Override
    protected ExampleServiceRequestContext preActivity(final I input) {
        return new ExampleServiceRequestContext(System.nanoTime());
    }

    @Override
    protected void postActivity(final O output, final ExampleServiceRequestContext requestContext) {
        final long endTime = System.nanoTime();
        final long spentTime = endTime - requestContext.getStartingTime();
        log.info("[{}] Activity took {} ms", requestContext.getRequestId(), spentTime / 1000000);
    }
}
