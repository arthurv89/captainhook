package com.arthurvlug._service.client;

import com.arthurvlug._service.common.ServiceConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.arthurvlug.captainhook.framework.client.AbstractClientConfiguration;

@NoArgsConstructor
public class ClientConfigurationComponent extends AbstractClientConfiguration {
    @Getter
    private final String baseUrl = ServiceConfiguration.baseUrl;
}
