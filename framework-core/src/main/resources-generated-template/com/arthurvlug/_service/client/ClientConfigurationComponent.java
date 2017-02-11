package com.arthurvlug._service.client;

import com.arthurvlug._service.ServiceConfiguration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.arthurvlug.captainhook.framework.client.AbstractClientConfiguration;
import org.springframework.stereotype.Component;

//@Component("_EndpointClientConfigurationComponent")
@NoArgsConstructor
public class ClientConfigurationComponent extends AbstractClientConfiguration {
    @Getter
    private final String baseUrl = ServiceConfiguration.baseUrl;
}
