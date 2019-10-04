package com.swipecrowd._service.server;

import com.swipecrowd.captainhook.framework.common.response.Output;
import com.swipecrowd.captainhook.framework.common.response.Response;
import com.swipecrowd.captainhook.framework.server.AbstractServerProperties;
import com.swipecrowd.captainhook.framework.server.IOType;
import com.swipecrowd.captainhook.framework.server.Input;
import com.swipecrowd.captainhook.framework.server.Request;
import com.google.common.collect.ImmutableMap;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.util.Map;

public class ServerProperties extends AbstractServerProperties {
    private static final String _EndpointEndpoint = "_Endpoint";

    @Getter
    private final String packageName = "com.swipecrowd._service";

    @Getter
    private final String serverName = "[name]";

    @Getter
    private final int port = Integer.parseInt("[port]");


    @Getter
    protected Map<String, IOType<?, ?>> iOTypes = createIoTypesMap();

    private ImmutableMap<String, IOType<? extends Input, ? extends Output>> createIoTypesMap() {
        return ImmutableMap.<String, IOType<? extends Input, ? extends Output>>builder()
                .put(_EndpointEndpoint, new IOType<>(new TypeToken<Request<com.swipecrowd._service.activity._endpoint._EndpointInput>>() {}, new TypeToken<Response<com.swipecrowd._service.activity._endpoint._EndpointOutput>>() {}))
                .build();
    }
}
