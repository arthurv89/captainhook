package com.swipecrowd.captainhook.tutorial.testservice.client;

import com.swipecrowd.captainhook.framework.client.AbstractClient;
import com.swipecrowd.captainhook.framework.common.response.Response;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import rx.Observable;

public class JavaClient extends AbstractClient {
    @Getter
    private static final int port = Integer.parseInt("1234");
    private static final String host = "1.2.3.4";

    @Override
    protected String getBaseUrl() {
        return "http://" + host + ":" + port;
    }

    public Observable<com.swipecrowd.captainhook.tutorial.testservice.activity.helloworld.HelloWorldOutput> helloWorldCall(final com.swipecrowd.captainhook.tutorial.testservice.activity.helloworld.HelloWorldInput input) {
        return createCall("HelloWorld", input, new TypeToken<Response<com.swipecrowd.captainhook.tutorial.testservice.activity.helloworld.HelloWorldOutput>>() {});
    }
}
