package com.arthurvlug._service.common;

import com.arthurvlug._service.ServiceConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.arthurvlug.captainhook.framework.server.AbstractCommonConfiguration;

@NoArgsConstructor
public class CommonConfiguration extends AbstractCommonConfiguration {
    @Getter
    private final String packageName = ServiceConfiguration.PACKAGE_NAME;
}
