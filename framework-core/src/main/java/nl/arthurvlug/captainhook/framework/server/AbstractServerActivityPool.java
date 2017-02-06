package nl.arthurvlug.captainhook.framework.server;

import nl.arthurvlug.captainhook.framework.common.response.Output;

public abstract class AbstractServerActivityPool {
    protected abstract <I extends Input, O extends Output, RC extends AbstractRequestContext> ServerActivityConfig<I, O, RC> get(final String activityName);
}
