package com.arthurvlug._service.server;

import com.arthurvlug._service.ServiceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {
        "nl.arthurvlug.captainhook.framework.server",
        "nl.arthurvlug.captainhook.framework.common",
        ServiceConfiguration.PACKAGE_NAME + ".common",
        ServiceConfiguration.PACKAGE_NAME + ".server"
})
public class Controller extends nl.arthurvlug.captainhook.framework.server.Controller {
    @Autowired
    public Controller(final ServerActivityPool serverActivityPool) {
        serverActivityPool.init();
    }

    public static void run(final String[] args) {
        SpringApplication.run(Controller.class, args);
    }
}
