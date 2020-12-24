package com.swipecrowd.captainhook.framework.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class MyApplication {
    private static AbstractServerProperties serverProperties;

    public synchronized static ConfigurableApplicationContext start(
            final AbstractServerProperties serverProperties,
            final AbstractGeneratedServerProperties generatedServerProperties,
            final Object[] sources,
            final String[] args) {

        MyApplication.serverProperties = serverProperties;
        return SpringApplication.run(
                sources,
                args);
    }

    @Bean
    public AbstractServerProperties createServerProperties() {
        return serverProperties;
    }

}
