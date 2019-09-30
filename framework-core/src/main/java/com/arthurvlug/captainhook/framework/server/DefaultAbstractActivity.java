package com.arthurvlug.captainhook.framework.server;

import com.arthurvlug.captainhook.framework.common.response.Output;

public abstract class DefaultAbstractActivity<I extends Input, O extends Output>
        extends AbstractActivity<I, O, DefaultRequestContext> {

    public DefaultRequestContext preActivity(I input) {
        return new DefaultRequestContext();
    }

    public void postActivity(final O output, final DefaultRequestContext requestContext) {}
}
