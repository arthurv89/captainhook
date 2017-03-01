package com.arthurvlug._service.server;

import lombok.Getter;
import nl.arthurvlug.captainhook.framework.server.generation.AbstractServerSpringComponentsImporter;
import org.springframework.context.annotation.Import;

@Import({
        ServerSpringComponents.class,
        nl.arthurvlug.captainhook.framework.common.AbstractClientWrapperSpringComponents.class
})
public class ServerSpringComponentsImporter extends AbstractServerSpringComponentsImporter {
        @Getter
        private final ServiceConfiguration serviceConfiguration = new ServiceConfiguration();
}
