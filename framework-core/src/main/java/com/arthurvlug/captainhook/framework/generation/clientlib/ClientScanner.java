package com.arthurvlug.captainhook.framework.generation.clientlib;

import com.arthurvlug.captainhook.framework.server.AbstractClientSpringComponents;
import org.reflections.Reflections;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientScanner {
    public static Set<Class<? extends AbstractClientSpringComponents>> run(final String ignorePackage) throws IOException {
        return new Reflections("").getSubTypesOf(AbstractClientSpringComponents.class)
                .stream()
                .filter(clientClass -> !clientClass.getName().equals(AbstractClientSpringComponents.class.getName() + ".class")) // Filter out the prototype client
                .filter(clientClass -> !clientClass.getName().startsWith(ignorePackage)) // Filter out the server's own client for upstreams
                .collect(Collectors.toSet());
    }
}