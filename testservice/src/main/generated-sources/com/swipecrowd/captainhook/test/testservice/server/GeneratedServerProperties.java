package com.swipecrowd.captainhook.test.testservice.server;

import com.swipecrowd.captainhook.framework.application.common.response.Output;
import com.swipecrowd.captainhook.framework.application.common.response.Response;
import com.swipecrowd.captainhook.framework.application.server.AbstractGeneratedServerProperties;
import com.swipecrowd.captainhook.framework.application.common.IOType;
import com.swipecrowd.captainhook.framework.application.common.Input;
import com.swipecrowd.captainhook.framework.application.common.Request;
import com.google.common.collect.ImmutableMap;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.util.Map;

public class GeneratedServerProperties extends AbstractGeneratedServerProperties {
    private static final String HelloWorldEndpoint = "HelloWorld";

    @Getter
    private final String packageName = "com.swipecrowd.captainhook.test.testservice";

    @Getter
    protected Map<String, IOType<?, ?>> iOTypes = createIoTypesMap();

    private ImmutableMap<String, IOType<? extends Input, ? extends Output>> createIoTypesMap() {
        return ImmutableMap.<String, IOType<? extends Input, ? extends Output>>builder()
                .put(HelloWorldEndpoint, new IOType<>(new TypeToken<Request<com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldInput>>() {}, new TypeToken<Response<com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldOutput>>() {}))
                .build();
    }
}
