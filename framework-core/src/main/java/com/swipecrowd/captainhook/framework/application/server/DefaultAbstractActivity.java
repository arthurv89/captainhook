package com.swipecrowd.captainhook.framework.application.server;

import com.swipecrowd.captainhook.framework.application.common.AbstractActivity;
import com.swipecrowd.captainhook.framework.application.common.DefaultRequestContext;
import com.swipecrowd.captainhook.framework.application.common.Input;
import com.swipecrowd.captainhook.framework.application.common.Output;

public abstract class DefaultAbstractActivity<I extends Input, O extends Output>
        extends AbstractActivity<I, O, DefaultRequestContext> {

    public DefaultRequestContext preActivity(I input) {
        return new DefaultRequestContext();
    }

    public void postActivity(final I input, final O output, final DefaultRequestContext requestContext) {}
}
