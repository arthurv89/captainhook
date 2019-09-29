package com.arthurvlug._service.server;

import com.arthurvlug._service.common.CommonConfiguration;
import lombok.Getter;
import nl.arthurvlug.captainhook.framework.server.AbstractServerSpringComponentsImporter;
import org.springframework.context.annotation.Import;

@Import({
        ServerSpringComponents.class,
        nl.arthurvlug.captainhook.framework.server.AbstractClientSpringComponents.class
})
public class ServerSpringComponentsImporter extends AbstractServerSpringComponentsImporter {
        @Getter
        private final CommonConfiguration commonConfiguration = new CommonConfiguration();
}
