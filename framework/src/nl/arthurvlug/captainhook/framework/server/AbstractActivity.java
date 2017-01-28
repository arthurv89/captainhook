package nl.arthurvlug.captainhook.framework.server;

import nl.arthurvlug.captainhook.framework.common.response.Output;

public abstract class AbstractActivity<I extends Input, O extends Output, RC extends AbstractRequestContext> {
    protected abstract O enact(I input) throws InterruptedException;

    protected abstract RC preActivity(I input);

    protected void postActivity(final O output, final RC requestContext) {}
}
