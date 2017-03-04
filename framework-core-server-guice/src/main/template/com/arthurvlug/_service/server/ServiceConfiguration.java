package com.arthurvlug._service.server;

import com.google.common.collect.ImmutableMap;
import com.google.gson.reflect.TypeToken;
import nl.arthurvlug.captainhook.framework.common.IOType;
import nl.arthurvlug.captainhook.framework.common.Input;
import nl.arthurvlug.captainhook.framework.common.Request;
import nl.arthurvlug.captainhook.framework.common.response.Output;
import nl.arthurvlug.captainhook.framework.common.response.Response;
import nl.arthurvlug.captainhook.framework.server.AbstractServiceConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Configuration
@Component
public class ServiceConfiguration extends AbstractServiceConfiguration {
    private static final String name = "[name]";
    private static final String port = "[port]";
    private static final String baseUrl = "http://localhost:" + port;
    private static final String packageName = "com.arthurvlug._service";

    public static final String _EndpointEndpoint = "_Endpoint";
    private static final ImmutableMap<String, IOType<? extends Input, ? extends Output>> activityMap =
            ImmutableMap.<String, IOType<? extends Input, ? extends Output>> builder()
                    .put(_EndpointEndpoint, new IOType<>(new TypeToken<Request<com.arthurvlug._service.activity._endpoint._EndpointInput>>() {}, new TypeToken<Response<com.arthurvlug._service.activity._endpoint._EndpointOutput>>() {}))
                    .build();

    public ServiceConfiguration() {
        super(name, port, baseUrl, packageName, activityMap);
    }
}
