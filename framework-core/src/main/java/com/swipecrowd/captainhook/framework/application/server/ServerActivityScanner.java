package com.swipecrowd.captainhook.framework.application.server;

import lombok.AllArgsConstructor;
import org.reflections.Reflections;

import java.util.Set;

/**
 * Scans the activities of the current service.
 */
@AllArgsConstructor
public class ServerActivityScanner {
    private final AbstractGeneratedServerProperties serverProperties;

    public Set<Class<? extends SimpleActivity>> scan() {
        final String packageName = serverProperties.getPackageName();
        return new Reflections(packageName).getSubTypesOf(SimpleActivity.class);
    }
}
