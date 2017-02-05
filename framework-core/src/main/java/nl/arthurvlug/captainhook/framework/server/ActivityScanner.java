package nl.arthurvlug.captainhook.framework.server;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityScanner {
    @Autowired
    private AbstractCommonConfiguration commonConfiguration;

    public Map<String, AbstractActivity> scan() {
        final String packageName = commonConfiguration.getPackageName();

        return getStringAbstractActivityMap(packageName).stream()
                .map(x -> x.load())
                .filter(x -> x.getAnnotationsByType(Activity.class).length != 0)
                .collect(Collectors.toMap(c -> c.getSimpleName().replace("Activity", ""), this::newInstance));
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
