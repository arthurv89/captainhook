package nl.arthurvlug.captainhook.framework.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.arthurvlug.captainhook.framework.common.response.Output;

import java.util.Map;

@AllArgsConstructor
public class AbstractActivityConfiguration {
    @Getter
    private final Map<String, IOType<? extends Input, ? extends Output>> map;

    public IOType<? extends Input, ? extends Output> getIOType(final String key) {
        return map.get(key);
    }
}
