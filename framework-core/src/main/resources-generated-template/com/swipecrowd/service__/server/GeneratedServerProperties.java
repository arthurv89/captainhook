package com.swipecrowd.service__.server;

import com.swipecrowd.captainhook.framework.application.common.Output;
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
    private static final String Endpoint__Endpoint = "Endpoint__";

    @Getter
    private final String packageName = "com.swipecrowd.service__";

    @Getter
    protected Map<String, IOType<?, ?>> iOTypes = createIoTypesMap();

    private ImmutableMap<String, IOType<? extends Input, ? extends Output>> createIoTypesMap() {
        return ImmutableMap.<String, IOType<? extends Input, ? extends Output>>builder()
                .put(Endpoint__Endpoint, new IOType<>(new TypeToken<Request<com.swipecrowd.service__.activity.endpoint__.Endpoint__Input>>() {}, new TypeToken<Response<com.swipecrowd.service__.activity.endpoint__.Endpoint__Output>>() {}))
                .build();
    }
}
