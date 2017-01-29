package nl.arthurvlug.captainhook.exampleservice.common;

import lombok.*;
import nl.arthurvlug.captainhook.exampleservice.ServiceConfiguration;
import nl.arthurvlug.captainhook.framework.server.AbstractCommonConfiguration;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonConfiguration extends AbstractCommonConfiguration {
    @Getter
    private final String packageName = ServiceConfiguration.PACKAGE_NAME;
}
