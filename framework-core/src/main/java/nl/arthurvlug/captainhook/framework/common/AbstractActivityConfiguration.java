package nl.arthurvlug.captainhook.framework.common;

import nl.arthurvlug.captainhook.framework.common.response.Output;

import java.util.Map;

public abstract class AbstractActivityConfiguration {
    public IOType<Input, Output> getIOType(final String key) {
        return getMap().get(key);
    }

    public abstract <I extends Input, O extends Output> Map<String, IOType<I, O>> getMap();
}
