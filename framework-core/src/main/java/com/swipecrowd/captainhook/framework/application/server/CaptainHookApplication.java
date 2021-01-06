package com.swipecrowd.captainhook.framework.application.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.Collection;

public class CaptainHookApplication {
    public static ConfigurableApplicationContext run(
            final AbstractGeneratedServerProperties generatedServerProperties,
            final Class<? extends DefaultServiceConfiguration> serviceConfiguration,
            final String[] args) throws Exception {
        return new CaptainHookApplication()._run(generatedServerProperties, serviceConfiguration, args);
    }

    private ConfigurableApplicationContext _run(
            final AbstractGeneratedServerProperties generatedServerProperties,
            final Class<? extends DefaultServiceConfiguration> serviceConfigurationClass,
            final String[] args) throws IOException {

        final ApplicationArguments properties = ApplicationArguments.create(args);

        final Class<?>[] sources = getSources(serviceConfigurationClass, generatedServerProperties);
        return MyApplication.start(properties, sources, args);
    }

    private Class<?>[] getSources(final Class<? extends DefaultServiceConfiguration> serviceConfigurationClass,
                                  final AbstractGeneratedServerProperties generatedServerProperties) {
        final Collection<Class<?>> classSet = ImmutableSet.<Class<?>>builder()
                .add(generatedServerProperties.getClass())
                .add(MyApplication.class)
                .add(Controller.class)
                .add(serviceConfigurationClass)
                .build();
        return toArray(classSet);
    }

    private Class<?>[] toArray(final Collection<Class<?>> objects) {
        return ImmutableList.<Class<?>>builder()
                .addAll(objects)
                .build()
                .toArray(new Class[objects.size()]);
    }
}
