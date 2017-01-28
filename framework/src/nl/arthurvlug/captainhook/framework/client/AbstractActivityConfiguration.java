package nl.arthurvlug.captainhook.framework.client;

import nl.arthurvlug.captainhook.framework.server.IOType;

import java.util.Map;

public abstract class AbstractActivityConfiguration {
    public IOType<?, ?> getIOType(final String key) {
        return getMap().get(key);
    }

    protected abstract Map<String, IOType<?, ?>> getMap();
}
