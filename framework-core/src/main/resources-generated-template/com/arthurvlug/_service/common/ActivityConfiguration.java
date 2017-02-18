package com.arthurvlug._service.common;

import com.google.common.collect.ImmutableMap;
import com.google.gson.reflect.TypeToken;
import lombok.NoArgsConstructor;
import nl.arthurvlug.captainhook.framework.client.AbstractActivityConfiguration;
import nl.arthurvlug.captainhook.framework.common.response.Output;
import nl.arthurvlug.captainhook.framework.common.response.Response;
import nl.arthurvlug.captainhook.framework.server.IOType;
import nl.arthurvlug.captainhook.framework.server.Input;
import nl.arthurvlug.captainhook.framework.server.Request;
import org.springframework.stereotype.Component;

import java.util.Map;

@NoArgsConstructor
public class ActivityConfiguration extends AbstractActivityConfiguration {
    public static final String _EndpointEndpoint = "_Endpoint";

    protected Map<String, IOType<?, ?>> getMap() {
        return ImmutableMap.<String, IOType<? extends Input, ? extends Output>> builder()
                .put(_EndpointEndpoint, new IOType<>(new TypeToken<Request<com.arthurvlug._service.activity._endpoint._EndpointInput>>() {}, new TypeToken<Response<com.arthurvlug._service.activity._endpoint._EndpointOutput>>() {}))
        .build();
    }
}
