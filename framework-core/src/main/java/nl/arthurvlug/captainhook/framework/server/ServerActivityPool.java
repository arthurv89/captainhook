package nl.arthurvlug.captainhook.framework.server;

import lombok.AllArgsConstructor;
import nl.arthurvlug.captainhook.framework.client.AbstractActivityConfiguration;
import nl.arthurvlug.captainhook.framework.common.response.Output;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ServerActivityPool extends AbstractServerActivityPool {
    private static final Map<String, ServerActivityConfig> map = new HashMap<>();
    private final AbstractActivityConfiguration activityConfiguration;

    @Override
    protected <I extends Input, O extends Output, RC extends AbstractRequestContext> ServerActivityConfig<I, O, RC> get(final String activity) {
        return map.get(activity);
    }

    public <I extends Input, O extends Output, RC extends AbstractRequestContext> void registerActivity(final AbstractActivity<I, O, RC> activity) {
        final String key = activity.getClass().getSimpleName().replace("Activity", "");
        final IOType<I, O> ioType = (IOType<I, O>) activityConfiguration.getIOType(key);
        final ServerActivityConfig<I, O, RC> serverActivityConfig = new ServerActivityConfig<>(activity, ioType.getRequestTypeToken());

        map.put(key, serverActivityConfig);
    }
}
