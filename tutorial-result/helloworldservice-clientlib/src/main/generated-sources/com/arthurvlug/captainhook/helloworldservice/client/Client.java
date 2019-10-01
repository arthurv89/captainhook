package com.arthurvlug.captainhook.helloworldservice.client;

import com.arthurvlug.captainhook.framework.client.AbstractClient;
import com.arthurvlug.captainhook.framework.common.response.Response;
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

    
}
