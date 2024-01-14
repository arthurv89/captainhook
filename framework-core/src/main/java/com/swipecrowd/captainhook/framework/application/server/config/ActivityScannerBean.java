package com.swipecrowd.captainhook.framework.application.server.config;

import com.swipecrowd.captainhook.framework.application.server.AbstractGeneratedServerProperties;
import com.swipecrowd.captainhook.framework.application.server.AbstractServerProperties;
import com.swipecrowd.captainhook.framework.application.server.ServerActivityScanner;
import com.swipecrowd.captainhook.framework.application.server.SimpleActivity;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.Set;

@AllArgsConstructor
public class ActivityScannerBean implements BeanDefinitionRegistryPostProcessor {
    final AbstractGeneratedServerProperties generatedServerProperties;
    final AbstractServerProperties serverProperties;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        final Set<Class<? extends SimpleActivity>> activityClasses = new ServerActivityScanner()
                .scan(generatedServerProperties.getPackageName());
        for(final Class<?> activityClass : activityClasses) {
            GenericBeanDefinition gbd = new GenericBeanDefinition();
            gbd.setBeanClass(activityClass);
            int port = serverProperties.getPort();

            String beanName = String.format("[%d] %s_Bean", port, activityClass.getName());
            registry.registerBeanDefinition(beanName, gbd);

            System.out.printf("Registering bean (activity) at port %d : %s%n", port,   beanName);

        }
    }

    @Override
    public void postProcessBeanFactory (ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //no op
    }
}