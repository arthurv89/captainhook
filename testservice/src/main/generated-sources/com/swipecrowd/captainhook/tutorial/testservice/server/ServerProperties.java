package com.swipecrowd.captainhook.tutorial.testservice.server;

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
    private static final String HelloWorldEndpoint = "HelloWorld";

    @Getter
    private final String packageName = "com.swipecrowd.captainhook.tutorial.testservice";

    @Getter
    private final String serverName = "1.2.3.4";

    @Getter
    private final int port = Integer.parseInt("1234");


    @Getter
    protected Map<String, IOType<?, ?>> iOTypes = createIoTypesMap();

    private ImmutableMap<String, IOType<? extends Input, ? extends Output>> createIoTypesMap() {
        return ImmutableMap.<String, IOType<? extends Input, ? extends Output>>builder()
                .put(HelloWorldEndpoint, new IOType<>(new TypeToken<Request<com.swipecrowd.captainhook.tutorial.testservice.activity.helloworld.HelloWorldInput>>() {}, new TypeToken<Response<com.swipecrowd.captainhook.tutorial.testservice.activity.helloworld.HelloWorldOutput>>() {}))
                .build();
    }
}
