package nl.arthurvlug.captainhook.[serviceName].client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.arthurvlug.captainhook.[serviceName].ServiceConfiguration;
import nl.arthurvlug.captainhook.framework.client.AbstractClientConfiguration;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class [ServiceName]ClientConfigurationComponent extends AbstractClientConfiguration {
    @Getter
    private final String baseUrl = ServiceConfiguration.baseUrl;
}
