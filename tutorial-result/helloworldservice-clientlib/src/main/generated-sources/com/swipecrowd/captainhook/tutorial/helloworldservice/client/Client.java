package com.swipecrowd.captainhook.tutorial.helloworldservice.client;

import com.swipecrowd.captainhook.framework.client.AbstractClient;
import com.swipecrowd.captainhook.framework.common.response.Response;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import rx.Observable;

public class Client extends AbstractClient {
    @Getter
    private static final int port = Integer.parseInt("8080");

    @Override
    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }

    public Observable<com.swipecrowd.captainhook.tutorial.helloworldservice.activity.helloworld.HelloWorldOutput> helloWorldCall(final com.swipecrowd.captainhook.tutorial.helloworldservice.activity.helloworld.HelloWorldInput input) {
        return createCall("HelloWorld", input, new TypeToken<Response<com.swipecrowd.captainhook.tutorial.helloworldservice.activity.helloworld.HelloWorldOutput>>() {});
    }
}
