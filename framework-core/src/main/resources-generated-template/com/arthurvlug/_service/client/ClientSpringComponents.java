package com.arthurvlug._service.client;

import com.arthurvlug._service.common.ActivityConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientSpringComponents {
    @Bean(name = "_EndpointClient")
    public Client getClient(@Qualifier("_EndpointClientActivityPool") final ClientActivityPool clientActivityPool,
                     @Qualifier("_EndpointClientConfigurationComponent") final ClientConfigurationComponent clientConfigurationComponent) {
        return new Client(clientActivityPool, clientConfigurationComponent);
    }

    @Bean(name = "_EndpointClientActivityPool")
    public ClientActivityPool getClientActivityPool(@Qualifier("_EndpointActivityConfiguration") final ActivityConfiguration activityConfiguration) {
        return new ClientActivityPool(activityConfiguration);
    }

    @Bean(name = "_EndpointActivityConfiguration")
    public ActivityConfiguration getActivityConfiguration() {
        return new ActivityConfiguration();
    }

    @Bean(name = "_EndpointClientConfigurationComponent")
    public ClientConfigurationComponent getClientConfigurationComponent() {
        return new ClientConfigurationComponent();
    }
}
