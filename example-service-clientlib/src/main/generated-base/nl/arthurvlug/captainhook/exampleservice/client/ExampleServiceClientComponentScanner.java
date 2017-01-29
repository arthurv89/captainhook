package nl.arthurvlug.captainhook.exampleservice.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.arthurvlug.captainhook.exampleservice.ServiceConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages= {
        ServiceConfiguration.PACKAGE_NAME + ".client",
        ServiceConfiguration.PACKAGE_NAME + ".common"
})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExampleServiceClientComponentScanner {
}
