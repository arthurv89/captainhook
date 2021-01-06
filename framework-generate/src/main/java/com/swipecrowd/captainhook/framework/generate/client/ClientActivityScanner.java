package com.swipecrowd.captainhook.framework.generate.client;

import com.swipecrowd.captainhook.framework.application.common.Input;
import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

public class ClientActivityScanner {
    public static Set<String> run(final String packageName) {
        return new Reflections(packageName)
                .getSubTypesOf(Input.class)
                .stream()
                .map(c -> c.getSimpleName().replace("Input", ""))
                .collect(Collectors.toSet());
    }
}