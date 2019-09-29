package com.arthurvlug._service.server;

import com.arthurvlug.captainhook.framework.common.response.Output;
import com.arthurvlug.captainhook.framework.common.response.Response;
import com.arthurvlug.captainhook.framework.server.*;
import com.google.common.collect.ImmutableMap;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.util.Map;

public class ServerProperties extends AbstractServerProperties {
    public static final String _EndpointEndpoint = "_Endpoint";

    @Getter
    private final String packageName = "com.arthurvlug._service";

    @Getter
    private final String serverName = "[name]";

    @Getter
    private final int port = Integer.parseInt("[port]");


    @Getter
    protected Map<String, IOType<?, ?>> endpointIOTypeMap = ImmutableMap.<String, IOType<? extends Input, ? extends Output>> builder()
                .put(_EndpointEndpoint, new IOType<>(new TypeToken<Request<com.arthurvlug._service.activity._endpoint._EndpointInput>>() {}, new TypeToken<Response<com.arthurvlug._service.activity._endpoint._EndpointOutput>>() {}))
                .build();

    @Override
    public Map<String, IOType<?, ?>> getEndpointIOTypeMap() {
        return endpointIOTypeMap;
    }
}
