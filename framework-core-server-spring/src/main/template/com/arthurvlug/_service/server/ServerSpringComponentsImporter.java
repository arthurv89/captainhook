package com.arthurvlug._service.server;

import lombok.Getter;
import nl.arthurvlug.captainhook.framework.common.AbstractClientWrapperProvider;
import nl.arthurvlug.captainhook.framework.server.AbstractServerComponentsImporter;
import org.springframework.context.annotation.Import;

@Import({
        ServiceConfiguration.class,
        AbstractClientWrapperProvider.class
})
public class ServerSpringComponentsImporter implements AbstractServerComponentsImporter {
    @Getter
    private final ServiceConfiguration serviceConfiguration = new ServiceConfiguration();
}
