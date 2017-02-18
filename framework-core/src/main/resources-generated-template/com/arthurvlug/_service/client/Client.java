package com.arthurvlug._service.client;

import com.arthurvlug._service.common.ActivityConfiguration;
import nl.arthurvlug.captainhook.framework.client.AbstractClient;
import rx.Observable;

public class Client extends AbstractClient {
    public Client(final ClientActivityPool clientActivityPool,
                   final ClientConfigurationComponent clientConfigurationComponent) {
        super(clientActivityPool, clientConfigurationComponent);
    }

    public Observable<com.arthurvlug._service.activity._endpoint._EndpointOutput> _EndpointCall(final com.arthurvlug._service.activity._endpoint._EndpointInput input) {
        return createCall(ActivityConfiguration._EndpointEndpoint, input);
    }

    protected String getName() {
        return "_Endpoint";
    }
}
