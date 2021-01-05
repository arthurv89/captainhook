package com.swipecrowd.captainhook.framework.server;

import com.swipecrowd.captainhook.framework.common.response.Output;

public abstract class AbstractActivity<I extends Input, O extends Output, RC extends com.swipecrowd.captainhook.framework.generation.AbstractRequestContext> {
    protected abstract RC preActivity(I input);

    protected abstract O handle(I request);

    protected void postActivity(final I input, final O output, final RC requestContext) {}
}
