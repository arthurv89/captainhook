package com.arthurvlug._service.server;

import nl.arthurvlug.captainhook.framework.server.AbstractServerSpringComponents;
import nl.arthurvlug.captainhook.framework.server.ServerActivityPool;
import nl.arthurvlug.captainhook.framework.server.generation.ActivityScanner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class ServerSpringComponents extends AbstractServerSpringComponents {
    @Bean(name = "_EndpointActivityConfiguration")
    public ActivityConfiguration getActivityConfiguration() {
        return new ActivityConfiguration();
    }

    @Bean(name = "_EndpointServiceConfiguration")
    public ServiceConfiguration getServiceConfiguration() {
        return new ServiceConfiguration();
    }

    @Bean(name = "_EndpointActivityScanner")
    public ActivityScanner getActivityScanner(@Qualifier("_EndpointServiceConfiguration") ServiceConfiguration serviceConfiguration) {
        return new ActivityScanner(serviceConfiguration);
    }

    @Bean(name = "_EndpointServerActivityPool")
    public ServerActivityPool getServerActivityPool(@Qualifier("_EndpointActivityConfiguration") ActivityConfiguration activityConfiguration) {
        return new ServerActivityPool(activityConfiguration);
    }
}
