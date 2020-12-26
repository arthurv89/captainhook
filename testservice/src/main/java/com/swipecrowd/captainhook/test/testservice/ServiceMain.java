package com.swipecrowd.captainhook.test.testservice;

import com.swipecrowd.captainhook.framework.server.CaptainHookApplication;
import com.swipecrowd.captainhook.test.testservice.server.GeneratedServerProperties;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

public class ServiceMain {
    public static void main(String[] args) {
        System.out.println("Starting application with args " + Arrays.toString(args));
        String[] validArgs = Arrays.stream(args)
                .filter(x -> x.contains("="))
                .toArray(String[]::new);
        startApplication(validArgs);
    }

    public static ConfigurableApplicationContext startApplication(String[] args) throws RuntimeException {
        System.out.println("Starting application with args " + Arrays.toString(args));
        try {
            return CaptainHookApplication.run(
//                    null,
                    new GeneratedServerProperties(),
                    TestServiceConfiguration.class,
                    args);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
