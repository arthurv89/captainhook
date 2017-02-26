package nl.arthurvlug.captainhook.framework.common;

import org.reflections.Reflections;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class HackedActivityScanner {
    public static Set<String> run(final String packageName) throws IOException {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(packageName);
        System.out.println(new Reflections(packageName).getSubTypesOf(Input.class));
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        return new Reflections(packageName)
                .getSubTypesOf(Input.class)
                .stream()
                .map(c -> c.getSimpleName().replace("Input", ""))
                .collect(Collectors.toSet());
    }
}