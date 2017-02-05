package nl.arthurvlug.captainhook.[serviceName].server;

import nl.arthurvlug.captainhook.[serviceName].ServiceConfiguration;
import nl.arthurvlug.captainhook.framework.server.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {
        "nl.arthurvlug.captainhook.framework.server",
        "nl.arthurvlug.captainhook.framework.common",
        ServiceConfiguration.PACKAGE_NAME + ".common",
        ServiceConfiguration.PACKAGE_NAME + ".server"
})
public class [ServiceName]Controller extends Controller {
    @Autowired
    public [ServiceName]Controller(final [ServiceName]ServerActivityPool [serviceName]ServerActivityPool) {
        [serviceName]ServerActivityPool.init();
    }

    public static void run(final String[] args) {
        SpringApplication.run([ServiceName]Controller.class, args);
    }
}
