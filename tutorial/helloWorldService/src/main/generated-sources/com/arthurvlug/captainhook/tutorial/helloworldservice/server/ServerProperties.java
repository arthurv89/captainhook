package com.arthurvlug.captainhook.tutorial.helloworldservice.server;

import com.arthurvlug.captainhook.framework.common.response.Output;
import com.arthurvlug.captainhook.framework.common.response.Response;
import com.arthurvlug.captainhook.framework.server.AbstractServerProperties;
import com.arthurvlug.captainhook.framework.server.IOType;
import com.arthurvlug.captainhook.framework.server.Input;
import com.arthurvlug.captainhook.framework.server.Request;
import com.google.common.collect.ImmutableMap;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.util.Map;

public class ServerProperties extends AbstractServerProperties {
    private static final String HelloWorldEndpoint = "HelloWorld";

    @Getter
    private final String packageName = "com.arthurvlug.captainhook.tutorial.helloworldservice";

    @Getter
    private final String serverName = "Hello World Service";

    @Getter
    private final int port = Integer.parseInt("8080");


    @Getter
    protected Map<String, IOType<?, ?>> iOTypes = createIoTypesMap();

    private ImmutableMap<String, IOType<? extends Input, ? extends Output>> createIoTypesMap() {
        return ImmutableMap.<String, IOType<? extends Input, ? extends Output>>builder()
                .put(HelloWorldEndpoint, new IOType<>(new TypeToken<Request<com.arthurvlug.captainhook.tutorial.helloworldservice.activity.helloworld.HelloWorldInput>>() {}, new TypeToken<Response<com.arthurvlug.captainhook.tutorial.helloworldservice.activity.helloworld.HelloWorldOutput>>() {}))
                .build();
    }
}
