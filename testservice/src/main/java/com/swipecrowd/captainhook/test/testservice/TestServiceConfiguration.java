package com.swipecrowd.captainhook.test.testservice;

import com.swipecrowd.captainhook.framework.application.server.AbstractServerProperties;
import com.swipecrowd.captainhook.framework.application.server.ApplicationArguments;
import com.swipecrowd.captainhook.framework.application.server.DefaultServiceConfiguration;
import com.swipecrowd.captainhook.framework.application.server.resilience.ServiceCall;
import com.swipecrowd.captainhook.test.testservice.client.TestServiceJavaClient;
import com.swipecrowd.captainhook.test.testservice.server.activity.helloworld.HelloWorldService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestServiceConfiguration extends DefaultServiceConfiguration {
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
                                                     final TestServiceServerProperties testServiceServerProperties,
                                                     final ServiceCall serviceCall,
                                                     final HelloWorldCache helloWorldCache) {
        return new HelloWorldService(
                testServiceJavaClient,
                testServiceServerProperties,
                serviceCall,
                helloWorldCache);
    }

    @Bean
    public HelloWorldCache createCache(final AbstractServerProperties abstractServerProperties) {
        return new HelloWorldCache();
    }
}
