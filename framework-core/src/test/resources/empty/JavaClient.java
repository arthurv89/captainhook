package com.swipecrowd.captainhook.tutorial.helloworldservice.client;

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


}
