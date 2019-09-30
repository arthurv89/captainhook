package com.arthurvlug.captainhook.framework.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableAutoConfiguration
public class ClientApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ClientApplication.class);
    }

    @Autowired
    AbstractClientRunner abstractClientRunner;

    @PostConstruct
    private void postConstruct() {
        abstractClientRunner.run();
    }
}
