package com.swipecrowd.captainhook.framework.server;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Scans the activities of the current service.
 */
@AllArgsConstructor
public class ActivityScanner {
    private final AbstractServerProperties serverProperties;

    public List<Class<?>> scan() {
        final String packageName = serverProperties.getPackageName();

        return getStringAbstractActivityMap(packageName).stream()
                .map(x -> x.load())
                .filter(x -> x.getAnnotationsByType(Activity.class).length != 0)
                .collect(Collectors.toList());
    }

    private ImmutableSet<ClassPath.ClassInfo> getStringAbstractActivityMap(final String packageName) {
        try {
            final ClassLoader loader = Thread.currentThread().getContextClassLoader();
            final ClassPath classpath = ClassPath.from(loader); // scans the class path used by classloader
            return classpath.getTopLevelClassesRecursive(packageName);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    private AbstractActivity newInstance(final Class<?> c) {
        try {
            return (AbstractActivity) c.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException();
        }
    }
}
