package com.swipecrowd._service.client;

import com.swipecrowd.captainhook.framework.client.AbstractClient;
import com.swipecrowd.captainhook.framework.common.response.Response;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import rx.Observable;

public class Client extends AbstractClient {
    @Getter
    private static final int port = Integer.parseInt("[port]");
    private static final String host = "[host]";

    @Override
    protected String getBaseUrl() {
        return "http://" + host + ":" + port;
    }

    public Observable<com.swipecrowd._service.activity._endpoint._EndpointOutput> _EndpointCall(final com.swipecrowd._service.activity._endpoint._EndpointInput input) {
        return createCall("_Endpoint", input, new TypeToken<Response<com.swipecrowd._service.activity._endpoint._EndpointOutput>>() {});
    }
}
