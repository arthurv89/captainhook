package com.swipecrowd.captainhook.tutorial.hellomoonservice.client;

import com.swipecrowd.captainhook.framework.client.AbstractClient;
import com.swipecrowd.captainhook.framework.common.response.Response;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import rx.Observable;

public class Client extends AbstractClient {
    @Getter
    private static final int port = Integer.parseInt("8081");

    @Override
    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }

    public Observable<com.swipecrowd.captainhook.tutorial.hellomoonservice.activity.hellomoon.HelloMoonOutput> helloMoonCall(final com.swipecrowd.captainhook.tutorial.hellomoonservice.activity.hellomoon.HelloMoonInput input) {
        return createCall("HelloMoon", input, new TypeToken<Response<com.swipecrowd.captainhook.tutorial.hellomoonservice.activity.hellomoon.HelloMoonOutput>>() {});
    }
}
