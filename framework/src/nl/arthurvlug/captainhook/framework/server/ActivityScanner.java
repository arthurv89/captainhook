package nl.arthurvlug.captainhook.framework.server;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivityScanner {
    @Autowired
    private AbstractCommonConfiguration commonConfiguration;

    Map<String, AbstractActivity> scan() {
        final String packageName = commonConfiguration.getPackageName();

        final Map<String, AbstractActivity> map = new Reflections(packageName)
                .getTypesAnnotatedWith(Activity.class)
                .stream()
                .collect(Collectors.toMap(c -> c.getSimpleName().replace("Activity", ""), this::newInstance));
        return map;
    }

    private AbstractActivity newInstance(final Class<?> c) {
        try {
            return (AbstractActivity) c.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException();
        }
    }
}
