package com.swipecrowd.captainhook.framework.application.server;

import com.swipecrowd.captainhook.framework.application.common.Input;
import com.swipecrowd.captainhook.framework.application.common.response.Output;

public abstract class AbstractActivity<I extends Input, O extends Output, RC extends AbstractRequestContext> {
    protected abstract RC preActivity(I input);

    protected abstract O handle(I request);

    protected void postActivity(final I input, final O output, final RC requestContext) {}
}
