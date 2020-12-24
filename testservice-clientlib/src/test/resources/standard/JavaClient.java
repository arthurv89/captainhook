package com.swipecrowd.captainhook.test.testservice.client;

import com.swipecrowd.captainhook.framework.client.AbstractClient;
import com.swipecrowd.captainhook.framework.common.response.Response;
import com.google.gson.reflect.TypeToken;
import com.swipecrowd.captainhook.framework.server.AbstractServerProperties;
import rx.Observable;

public class JavaClient extends AbstractClient {
    public JavaClient(final AbstractServerProperties serverProperties) {
        super(serverProperties);
    }

    public Observable<com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldOutput> helloWorldCall(final com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldInput input) {
        return createCall("TestService", "HelloWorld", input, new TypeToken<Response<com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldOutput>>() {});
    }
}