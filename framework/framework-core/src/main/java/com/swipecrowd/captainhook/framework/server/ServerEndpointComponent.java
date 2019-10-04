package com.swipecrowd.captainhook.framework.server;

import com.swipecrowd.captainhook.framework.common.response.Output;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ServerEndpointComponent implements AbstractServerEndpointComponent {
    @Getter
    private static final Map<String, ServerActivityConfig<? extends Input, ? extends Output, ? extends AbstractRequestContext>> serverActivityConfigMap = new HashMap<>();

    @Getter
    private final AbstractServerProperties serverProperties;

    @Override
    public <I extends Input, O extends Output, RC extends AbstractRequestContext> ServerActivityConfig<I, O, RC> get(final String activity) {
        return (ServerActivityConfig<I, O, RC>) serverActivityConfigMap.get(activity);
    }

    public <I extends Input, O extends Output, RC extends AbstractRequestContext> void registerActivity(final AbstractActivity<I, O, RC> activity) {
        final String activityName = activity.getClass().getSimpleName().replace("Activity", "");
        final IOType<I, O> ioType = getIoType(activityName);
        final ServerActivityConfig<I, O, RC> serverActivityConfig = new ServerActivityConfig<>(activity, ioType);

        serverActivityConfigMap.put(activityName, serverActivityConfig);
    }

    private <I extends Input, O extends Output> IOType<I, O> getIoType(final String activityName) {
        return (IOType<I, O>) serverProperties.getIOTypes().get(activityName);
    }
}
