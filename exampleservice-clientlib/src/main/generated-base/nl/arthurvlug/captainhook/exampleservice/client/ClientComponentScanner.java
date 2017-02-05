package nl.arthurvlug.captainhook.[serviceName].client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.arthurvlug.captainhook.[serviceName].ServiceConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages= {
        ServiceConfiguration.PACKAGE_NAME + ".client",
        ServiceConfiguration.PACKAGE_NAME + ".common"
})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class [ServiceName]ClientComponentScanner {
}
