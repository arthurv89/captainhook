package com.swipecrowd.captainhook.test.testservice.client;

import com.swipecrowd.captainhook.framework.application.common.AbstractClient;
import com.swipecrowd.captainhook.framework.application.common.response.Response;
import com.google.gson.reflect.TypeToken;
import com.swipecrowd.captainhook.framework.application.server.AbstractServerProperties;
import io.reactivex.rxjava3.core.Observable;

public class TestServiceJavaClient extends AbstractClient {
    public TestServiceJavaClient(final AbstractServerProperties serverProperties) {
        super(serverProperties);
    }

    public Observable<com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldOutput> helloWorldCall(final com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldInput input) {
        return createCall("TestService", "HelloWorld", input, new TypeToken<Response<com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldOutput>>() {});
    }
}