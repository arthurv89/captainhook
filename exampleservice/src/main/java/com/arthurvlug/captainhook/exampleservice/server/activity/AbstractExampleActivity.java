package com.arthurvlug.captainhook.exampleservice.server.activity;

import lombok.extern.slf4j.Slf4j;
import com.arthurvlug.captainhook.framework.common.response.Output;
import com.arthurvlug.captainhook.framework.server.AbstractActivity;
import com.arthurvlug.captainhook.framework.server.Input;

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