package nl.arthurvlug.captainhook.[serviceName].common;

import lombok.*;
import nl.arthurvlug.captainhook.[serviceName].ServiceConfiguration;
import nl.arthurvlug.captainhook.framework.server.AbstractCommonConfiguration;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class [ServiceName]CommonConfiguration extends AbstractCommonConfiguration {
    @Getter
    private final String packageName = ServiceConfiguration.PACKAGE_NAME;
}
