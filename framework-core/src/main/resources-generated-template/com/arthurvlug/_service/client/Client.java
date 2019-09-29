package com.arthurvlug._service.client;

import com.arthurvlug.captainhook.framework.client.AbstractClient;
import com.arthurvlug.captainhook.framework.client.ClientActivityConfig;
import com.arthurvlug.captainhook.framework.common.response.Output;
import com.arthurvlug.captainhook.framework.server.Input;
import lombok.Getter;
import rx.Observable;

import java.util.HashMap;
import java.util.Map;

public class Client extends AbstractClient {
    @Getter
    private static final int port = Integer.parseInt("[port]");

    @Override
    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @Getter
    private final Map<String, ClientActivityConfig<Input, Output>> activities = new HashMap<>();

    public Observable<com.arthurvlug._service.activity._endpoint._EndpointOutput> _EndpointCall(final com.arthurvlug._service.activity._endpoint._EndpointInput input) {
        return createCall("_Endpoint", input);
    }
}
