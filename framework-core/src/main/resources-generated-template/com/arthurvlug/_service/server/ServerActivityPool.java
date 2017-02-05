package com.arthurvlug._service.server;

import com.arthurvlug._service.common.ActivityConfiguration;
import nl.arthurvlug.captainhook.framework.client.AbstractActivityConfiguration;
import nl.arthurvlug.captainhook.framework.server.BaseServerActivityPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("_EndpointServerActivityPool")
public class ServerActivityPool extends BaseServerActivityPool {
    @Autowired
    private ActivityConfiguration activityConfiguration;

    protected AbstractActivityConfiguration getActivityConfiguration() {
        return activityConfiguration;
    }
}
