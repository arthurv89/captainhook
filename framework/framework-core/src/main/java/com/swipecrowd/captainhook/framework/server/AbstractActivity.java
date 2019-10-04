package com.swipecrowd.captainhook.framework.server;

import com.swipecrowd.captainhook.framework.common.response.Output;
import rx.Observable;

public abstract class AbstractActivity<I extends Input, O extends Output, RC extends AbstractRequestContext> {
    protected abstract RC preActivity(I input);

    protected abstract Observable<O> enact(I input);

    protected void postActivity(final I input, final O output, final RC requestContext) {}
}
