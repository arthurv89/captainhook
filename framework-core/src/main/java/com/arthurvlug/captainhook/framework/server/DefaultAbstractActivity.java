package com.arthurvlug.captainhook.framework.server;

import com.arthurvlug.captainhook.framework.common.response.Output;

public abstract class DefaultAbstractActivity<I extends Input, O extends Output>
        extends AbstractActivity<I, O, DefaultRequestContext> {

    protected DefaultRequestContext preActivity(I input) {
        return new DefaultRequestContext();
    }

    protected void postActivity(final O output, final DefaultRequestContext requestContext) {}
}
