package com.swipecrowd.captainhook.framework.server;

import lombok.AllArgsConstructor;
import org.reflections.Reflections;

import java.util.Set;

/**
 * Scans the activities of the current service.
 */
@AllArgsConstructor
public class ActivityScanner {
    private final AbstractGeneratedServerProperties serverProperties;

    public Set<Class<?>> scan() {
        final String packageName = serverProperties.getPackageName();
        return new Reflections(packageName).getTypesAnnotatedWith(Activity.class);
    }
}
