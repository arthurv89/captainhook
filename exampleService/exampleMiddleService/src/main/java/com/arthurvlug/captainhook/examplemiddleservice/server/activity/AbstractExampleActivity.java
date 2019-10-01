package com.arthurvlug.captainhook.examplemiddleservice.server.activity;

import lombok.extern.slf4j.Slf4j;
import com.arthurvlug.captainhook.framework.common.response.Output;
import com.arthurvlug.captainhook.framework.server.AbstractActivity;
import com.arthurvlug.captainhook.framework.server.Input;

@Slf4j
public abstract class AbstractExampleActivity<I extends Input, O extends Output>
        extends AbstractActivity<I, O, MiddleServiceRequestContext> {
    @Override
    protected MiddleServiceRequestContext preActivity(final I input) {
        return new MiddleServiceRequestContext(System.nanoTime());
    }

    @Override
    protected void postActivity(final I input, final O output, final MiddleServiceRequestContext requestContext) {
        final long endTime = System.nanoTime();
        final long spentTime = endTime - requestContext.getStartingTime();
        log.info("[{}] Activity took {} ms", requestContext.getRequestId(), spentTime / 1000000);
    }
}
