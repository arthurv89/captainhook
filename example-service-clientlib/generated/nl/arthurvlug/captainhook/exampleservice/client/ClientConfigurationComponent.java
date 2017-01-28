package nl.arthurvlug.captainhook.exampleservice.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.arthurvlug.captainhook.exampleservice.ServiceConfiguration;
import nl.arthurvlug.captainhook.framework.client.AbstractClientConfiguration;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientConfigurationComponent extends AbstractClientConfiguration {
    @Getter
    private final String baseUrl = ServiceConfiguration.baseUrl;
}
