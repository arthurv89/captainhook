package com.arthurvlug._service.server;

import com.arthurvlug._service.server.ServiceConfiguration;
import lombok.Getter;
import nl.arthurvlug.captainhook.framework.server.AbstractServerComponentsImporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({
        AbstractClientWrapperProvider.class
})
public class ServerSpringComponentsImporter implements AbstractServerComponentsImporter {
    @Getter(onMethod = @__(@Bean))
    private final ServiceConfiguration serviceConfiguration = new ServiceConfiguration();
}
