package nl.arthurvlug.captainhook.framework.server;

import nl.arthurvlug.captainhook.framework.client.AbstractActivityConfiguration;
import nl.arthurvlug.captainhook.framework.common.response.Output;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class BaseServerActivityPool extends AbstractServerActivityPool {
    private Map<String, ServerActivityConfig<? extends Input, ? extends Output>> map;

    @Autowired
    private ActivityScanner activityScanner;

    public void init() {
        final Map<String, ServerActivityConfig<? extends Input, ? extends Output>> collect = activityScanner.scan().entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey(),
                e -> activityConfig(e.getKey(), e.getValue())));
        map = collect;
    }

    private ServerActivityConfig<? extends Input, ? extends Output> activityConfig(final String key, final AbstractActivity value) {
        final IOType<? extends Input, ? extends Output> ioType = getActivityConfiguration().getIOType(key);
        return new ServerActivityConfig<>(value, ioType.getRequestTypeToken());
    }

    protected ServerActivityConfig<? extends Input, ? extends Output> get(final String activity) {
        return map.get(activity);
    }

    protected abstract AbstractActivityConfiguration getActivityConfiguration();
}
