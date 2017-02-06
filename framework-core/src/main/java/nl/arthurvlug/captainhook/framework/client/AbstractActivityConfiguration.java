package nl.arthurvlug.captainhook.framework.client;

import nl.arthurvlug.captainhook.framework.common.response.Output;
import nl.arthurvlug.captainhook.framework.server.IOType;
import nl.arthurvlug.captainhook.framework.server.Input;

import java.util.Map;

public abstract class AbstractActivityConfiguration {
    public IOType<Input, Output> getIOType(final String key) {
        return getMap().get(key);
    }

    protected abstract <I extends Input, O extends Output> Map<String, IOType<I, O>> getMap();
}
