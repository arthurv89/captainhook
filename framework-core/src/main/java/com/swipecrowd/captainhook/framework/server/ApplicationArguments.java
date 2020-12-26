package com.swipecrowd.captainhook.framework.server;

import com.swipecrowd.captainhook.framework.generation.clientlib.GenerateClientLibClasses;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureClassLoader;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@ToString
public class ApplicationArguments {
    public static final String PORT_KEY = "server.port";
    private final Map<String, String> map;

    public static ApplicationArguments create(final String[] args) throws IOException {
        final Map<String, String> map = new HashMap<>();
        map.putAll(toMap(args));
        map.putAll(toMap(getProperties()));

        return new ApplicationArguments(map);
    }

    private static Map<String, String> toMap(final String[] args) {
        final List<String> l = Arrays.stream(args)
                .filter(x -> !x.contains("="))
                .collect(Collectors.toList());
        if(l.size() > 0) {
            throw new IllegalArgumentException("Count not parse arguments (because they don't have an '=' symbol): " + l);
        }
        return Arrays.stream(args)
                .map(x -> x.split("="))
                .collect(Collectors.toMap(
                        arr -> arr[0].substring(2),
                        arr -> arr[1]));
    }

    private static Map<String, String> toMap(final Properties properties) {
        final Map<String, String> map = new HashMap<>();
        for (final String name: properties.stringPropertyNames()) {
            map.put(name, properties.getProperty(name));
        }
        return map;
    }

    private static Properties getProperties() throws IOException {
        final SecureClassLoader classLoader = (SecureClassLoader) GenerateClientLibClasses.class.getClassLoader();
        final String propertiesFile = "application.properties";

        final InputStream resourceAsStream = classLoader.getResourceAsStream(propertiesFile);

        final Properties properties = new Properties();
        properties.load(resourceAsStream);
        return properties;
    }

    public Optional<String> get(final String key) {
        return Optional.ofNullable(map.get(key));
    }
}
