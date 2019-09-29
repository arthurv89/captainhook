package com.arthurvlug.captainhook.framework.client;

import com.arthurvlug.captainhook.framework.server.ServerEndpointComponent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.arthurvlug.captainhook.framework.common.response.Output;
import com.arthurvlug.captainhook.framework.server.IOType;
import com.arthurvlug.captainhook.framework.server.Input;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseClientActivityPool {
    private Map<String, ClientActivityConfig<? extends Input, ? extends Output>> clientActivityMap;

    @PostConstruct
    private void init() {
        clientActivityMap = serverEndpointComponent().getServerActivityConfigMap().entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> activityConfig(e.getKey())
                ));
    }

    private ClientActivityConfig<? extends Input, ? extends Output> activityConfig(final String key) {
        final IOType<? extends Input, ? extends Output> ioType = serverEndpointComponent().getIOType(key);
        return new ClientActivityConfig<>(ioType.getRequestTypeToken(), ioType.getResponseTypeToken());
    }

    protected abstract ServerEndpointComponent serverEndpointComponent();
}
