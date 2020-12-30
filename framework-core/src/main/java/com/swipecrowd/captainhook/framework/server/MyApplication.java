package com.swipecrowd.captainhook.framework.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Collections;

import static com.swipecrowd.captainhook.framework.server.ApplicationArguments.PORT_KEY;

@SpringBootApplication
@Configuration
public class MyApplication {
    public synchronized static ConfigurableApplicationContext start(
            final ApplicationArguments applicationArguments,
            final Class<?>[] sources,
            final String[] args) {

        final String port = PropertiesUtils.getPort(applicationArguments);
        System.out.println("Running on port " + port);

        final SpringApplication app = new SpringApplication(sources);
        app.setDefaultProperties(Collections.singletonMap(PORT_KEY, port));
        return app.run(args);
    }


    @Bean
    public ApplicationArguments createApplicationArguments(org.springframework.boot.ApplicationArguments applicationArguments) throws IOException {
        final String[] args = applicationArguments.getSourceArgs();
        return ApplicationArguments.create(args);
    }

    @Bean
    public ServerEndpointComponent createServerEndpointComponent(final AbstractGeneratedServerProperties generatedServerProperties,
                                                                 final AbstractServerProperties serverProperties) {
        return new ServerEndpointComponent(generatedServerProperties, serverProperties);
    }

    @Bean
    public ActivityScannerBean createActivityScannerBean(final AbstractGeneratedServerProperties generatedServerProperties,
                                                         final AbstractServerProperties serverProperties) {
        return new ActivityScannerBean(generatedServerProperties, serverProperties);
    }
}
