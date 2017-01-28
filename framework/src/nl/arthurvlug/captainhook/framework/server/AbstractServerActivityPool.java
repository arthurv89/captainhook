package nl.arthurvlug.captainhook.framework.server;

public abstract class AbstractServerActivityPool {
    protected abstract ServerActivityConfig get(final String activityName);
}
