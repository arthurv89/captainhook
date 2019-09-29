package nl.arthurvlug.captainhook.framework.generation.clientlib;

import nl.arthurvlug.captainhook.framework.server.Input;
import org.reflections.Reflections;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class ActivityScanner {
    public static Set<String> run(final String packageName) throws IOException {
        return new Reflections(packageName)
                .getSubTypesOf(Input.class)
                .stream()
                .map(c -> c.getSimpleName().replace("Input", ""))
                .collect(Collectors.toSet());
    }
}