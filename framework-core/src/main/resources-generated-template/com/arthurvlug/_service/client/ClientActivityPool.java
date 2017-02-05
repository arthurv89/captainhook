package com.arthurvlug._service.client;

import com.arthurvlug._service.common.ActivityConfiguration;
import lombok.Getter;
import nl.arthurvlug.captainhook.framework.client.BaseClientActivityPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("_EndpointClientActivityPool")
@Getter
public class ClientActivityPool extends BaseClientActivityPool {
    @Autowired
    private ActivityConfiguration activityConfiguration;
}

