package com.arthurvlug._service.server;

import com.arthurvlug._service.common.ActivityConfiguration;
import com.arthurvlug._service.common.CommonConfiguration;
import nl.arthurvlug.captainhook.framework.server.AbstractServerSpringComponents;
import nl.arthurvlug.captainhook.framework.server.ActivityScanner;
import nl.arthurvlug.captainhook.framework.server.ServerActivityPool;
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

    @Bean(name = "_EndpointCommonConfiguration")
    public CommonConfiguration getCommonConfiguration() {
        return new CommonConfiguration();
    }

    @Bean(name = "_EndpointActivityScanner")
    public ActivityScanner getActivityScanner(@Qualifier("_EndpointCommonConfiguration") CommonConfiguration commonConfiguration) {
        return new ActivityScanner(commonConfiguration);
    }

    @Bean(name = "_EndpointServerActivityPool")
    public ServerActivityPool getServerActivityPool(@Qualifier("_EndpointActivityConfiguration") ActivityConfiguration activityConfiguration) {
        return new ServerActivityPool(activityConfiguration);
    }
}
