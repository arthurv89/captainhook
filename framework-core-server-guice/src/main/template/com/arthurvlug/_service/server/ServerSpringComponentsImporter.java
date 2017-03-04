package com.arthurvlug._service.server;

import lombok.Getter;
import nl.arthurvlug.captainhook.framework.common.AbstractClientWrapperProvider;
import nl.arthurvlug.captainhook.framework.server.spring.AbstractServerSpringComponentsImporter;
import org.springframework.context.annotation.Import;

@Import({
        ServiceConfiguration.class,
        AbstractClientWrapperProvider.class
})
public class ServerSpringComponentsImporter implements AbstractServerSpringComponentsImporter {
    @Getter
    private final ServiceConfiguration serviceConfiguration = new ServiceConfiguration();
}
