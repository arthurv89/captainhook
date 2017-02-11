package com.arthurvlug._service.client;

import com.arthurvlug._service.common.ActivityConfiguration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.arthurvlug.captainhook.framework.client.BaseClientActivityPool;
import org.springframework.stereotype.Component;

//@Component("_EndpointClientActivityPool")
@AllArgsConstructor
@Getter
public class ClientActivityPool extends BaseClientActivityPool {
    private ActivityConfiguration activityConfiguration;
}

