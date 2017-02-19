package com.arthurvlug._service.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.arthurvlug.captainhook.framework.server.AbstractCommonConfiguration;

@NoArgsConstructor
public class CommonConfiguration extends AbstractCommonConfiguration {
    @Getter
    private final String packageName = ServiceConfiguration.PACKAGE_NAME;

    @Getter
    private final String serverName = ServiceConfiguration.name;

    @Getter
    private final int port = Integer.parseInt(ServiceConfiguration.port);
}
