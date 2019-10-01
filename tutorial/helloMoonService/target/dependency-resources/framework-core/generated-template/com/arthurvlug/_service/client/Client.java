package com.arthurvlug._service.client;

import com.arthurvlug.captainhook.framework.client.AbstractClient;
import com.arthurvlug.captainhook.framework.common.response.Response;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import rx.Observable;

public class Client extends AbstractClient {
    @Getter
    private static final int port = Integer.parseInt("[port]");

    @Override
    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }

    public Observable<com.arthurvlug._service.activity._endpoint._EndpointOutput> _EndpointCall(final com.arthurvlug._service.activity._endpoint._EndpointInput input) {
        return createCall("_Endpoint", input, new TypeToken<Response<com.arthurvlug._service.activity._endpoint._EndpointOutput>>() {});
    }
}
