package com.arthurvlug.captainhook.framework.server;

import com.arthurvlug.captainhook.framework.common.response.Output;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ServerEndpointComponent implements AbstractServerEndpointComponent {
    @Getter
    private static final Map<String, ServerActivityConfig> serverActivityConfigMap = new HashMap<>();

    @Getter
    private final AbstractServerProperties serverProperties;

    @Override
    public <I extends Input, O extends Output, RC extends AbstractRequestContext> ServerActivityConfig<I, O, RC> get(final String activity) {
        return serverActivityConfigMap.get(activity);
    }

    public IOType<Input, Output> getIOType(final String key) {
        IOType<?, ?> ioType = serverProperties.getEndpointIOTypeMap().get(key);
        return (IOType<Input, Output>) ioType;
    }

    public <I extends Input, O extends Output, RC extends AbstractRequestContext> void registerActivity(final AbstractActivity<I, O, RC> activity) {
        final String activityName = activity.getClass().getSimpleName().replace("Activity", "");
        final IOType<I, O> ioType = (IOType<I, O>) getIOType(activityName);
        final ServerActivityConfig<I, O, RC> serverActivityConfig = new ServerActivityConfig<>(activity, ioType.getRequestTypeToken());

        serverActivityConfigMap.put(activityName, serverActivityConfig);
    }
}
