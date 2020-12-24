package com.swipecrowd.captainhook.test.testservice;

import com.swipecrowd.captainhook.framework.server.Controller;
import com.swipecrowd.captainhook.test.testservice.server.GeneratedServerProperties;
import org.springframework.context.ConfigurableApplicationContext;

public class ServiceMain {
    public static void main(String[] args) {
        startApplication(args);
    }

    public static ConfigurableApplicationContext startApplication(String[] args) throws RuntimeException {
        try {
            return Controller.run(
//                    null,
                    new GeneratedServerProperties(),
                    ServerProperties.class,
                    args);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
