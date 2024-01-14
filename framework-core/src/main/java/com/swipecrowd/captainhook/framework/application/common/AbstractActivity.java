package com.swipecrowd.captainhook.framework.application.common;

public abstract class AbstractActivity<I extends Input, O extends Output, RC extends AbstractRequestContext> {
    public abstract RC preActivity(I input);

    public abstract O handle(I request);

    public void postActivity(final I input, final O output, final RC requestContext) {}
}
