package nl.arthurvlug.captainhook.exampleservice.server;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.arthurvlug.captainhook.exampleservice.ServiceConfiguration;
import nl.arthurvlug.captainhook.framework.server.Controller;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {
        "nl.arthurvlug.captainhook.framework.server",
        "nl.arthurvlug.captainhook.framework.common",
        ServiceConfiguration.PACKAGE_NAME + ".common",
        ServiceConfiguration.PACKAGE_NAME + ".server"
})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExampleServiceController extends Controller {
    public static void run(final String[] args) {
        SpringApplication.run(ExampleServiceController.class, args);
    }
}
