package nl.arthurvlug.captainhook.framework.server;

import nl.arthurvlug.captainhook.framework.client.AbstractActivityConfiguration;
import nl.arthurvlug.captainhook.framework.common.response.Output;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class BaseServerActivityPool extends AbstractServerActivityPool {
    private Map<String, ServerActivityConfig<? extends Input, ? extends Output, ? extends AbstractRequestContext>> map;

    @Autowired
    private ActivityScanner activityScanner;

    public void init() {
        map = activityScanner.scan().entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey(),
                e -> activityConfig(e.getKey(), e.getValue())));
    }

    private <I extends Input, O extends Output, RC extends AbstractRequestContext> ServerActivityConfig<I, O, RC> activityConfig(final String key, final AbstractActivity<I, O, RC> activity) {
        final IOType<I, O> ioType = (IOType<I, O>) getActivityConfiguration().getIOType(key);
        return new ServerActivityConfig<>(activity, ioType.getRequestTypeToken());
    }

    @Override
    protected ServerActivityConfig<? extends Input, ? extends Output, ? extends AbstractRequestContext> get(final String activity) {
        return map.get(activity);
    }

    protected abstract AbstractActivityConfiguration getActivityConfiguration();
}
