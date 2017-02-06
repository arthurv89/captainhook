package com.arthurvlug._service.client;

import com.arthurvlug._service.common.ActivityConfiguration;
import nl.arthurvlug.captainhook.framework.client.AbstractClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

@Component("_EndpointClient")
public class Client extends AbstractClient {
    @Autowired
    private Client(final ClientActivityPool clientActivityPool,
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
