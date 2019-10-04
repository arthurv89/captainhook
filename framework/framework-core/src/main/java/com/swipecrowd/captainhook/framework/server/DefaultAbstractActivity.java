package com.swipecrowd.captainhook.framework.server;

import com.swipecrowd.captainhook.framework.common.response.Output;

public abstract class DefaultAbstractActivity<I extends Input, O extends Output>
        extends AbstractActivity<I, O, DefaultRequestContext> {

    public DefaultRequestContext preActivity(I input) {
        return new DefaultRequestContext();
    }

    public void postActivity(final I input, final O output, final DefaultRequestContext requestContext) {}
}
