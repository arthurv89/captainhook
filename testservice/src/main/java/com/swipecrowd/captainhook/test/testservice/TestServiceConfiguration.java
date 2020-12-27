package com.swipecrowd.captainhook.test.testservice;

import com.swipecrowd.captainhook.framework.server.AbstractServerProperties;
import com.swipecrowd.captainhook.framework.server.ApplicationArguments;
import com.swipecrowd.captainhook.framework.server.DefaultServiceConfiguration;
import com.swipecrowd.captainhook.test.testservice.client.TestServiceJavaClient;
import com.swipecrowd.captainhook.test.testservice.server.activity.helloworld.HelloWorldService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class TestServiceConfiguration extends DefaultServiceConfiguration {
    @Bean
    public ApplicationArguments createApplicationArguments(org.springframework.boot.ApplicationArguments applicationArguments) throws IOException {
        final String[] args = applicationArguments.getSourceArgs();
        return ApplicationArguments.create(args);
    }

    @Bean
    public TestServiceServerProperties createTestServiceServerProperties(final ApplicationArguments applicationArguments) {
        return new TestServiceServerProperties(applicationArguments);
    }

    @Bean
    public TestServiceJavaClient createJavaClient(final AbstractServerProperties serverProperties) {
        return new TestServiceJavaClient(serverProperties);
    }

    @Bean
    public HelloWorldService createHelloWorldService(final TestServiceJavaClient testServiceJavaClient,
                                                     final TestServiceServerProperties testServiceServerProperties) {
        return new HelloWorldService(testServiceJavaClient, testServiceServerProperties);
    }
}
