package com.swipecrowd.service__.client;

import com.swipecrowd.captainhook.framework.application.common.AbstractClient;
import com.swipecrowd.captainhook.framework.application.common.response.Response;
import com.google.gson.reflect.TypeToken;
import com.swipecrowd.captainhook.framework.application.server.AbstractServerProperties;
import io.reactivex.rxjava3.core.Observable;

public class TestServiceJavaClient extends AbstractClient {
    public TestServiceJavaClient(final AbstractServerProperties serverProperties) {
        super(serverProperties);
    }

    public Observable<com.swipecrowd.service__.activity.endpoint__.Endpoint__Output> endpoint__Call(final com.swipecrowd.service__.activity.endpoint__.Endpoint__Input input) {
        return createCall("Service__", "Endpoint__", input, new TypeToken<Response<com.swipecrowd.service__.activity.endpoint__.Endpoint__Output>>() {});
    }
}
