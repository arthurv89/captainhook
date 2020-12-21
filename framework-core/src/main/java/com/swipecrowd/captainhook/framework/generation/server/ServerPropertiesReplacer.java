package com.swipecrowd.captainhook.framework.generation.server;

import com.swipecrowd.captainhook.framework.generation.DefaultReplacer;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.swipecrowd.captainhook.framework.generation.Generator.getPackage;

public class ServerPropertiesReplacer extends DefaultReplacer {
    @Override
    public String replace(final String contents,
                          final String basePackage,
                          final String serviceName,
                          final Set<String> activities,
                          final String hostName,
                          final String hostPort) {
        final List<EntryConfig> endpointConfigs = getEntryConfigs(basePackage, serviceName, activities);
        final String newEndpointDeclarations = createEndpointDeclarations(endpointConfigs);
        final String newEntryDeclarations = createEntryDeclarations(endpointConfigs);
        final String newPackage = getPackage(basePackage, serviceName);

        final EntryConfig entryPrototype = createTemplateEntryConfig();
        final String prototypeEndpoint = endpoint(entryPrototype);
        final String prototypeEntry = entry(entryPrototype);
        final String prototypePackage = entryPrototype.getPackage();
        final String prototypeHostName = "[hostname]";
        final String prototypeHostPort = "[port]";

        String newContents = contents;
        newContents = doReplace(newContents, prototypeEndpoint, newEndpointDeclarations);
        newContents = doReplace(newContents, prototypeEntry, newEntryDeclarations);
        newContents = doReplace(newContents, prototypePackage, newPackage);
        newContents = doReplace(newContents, prototypeHostName, hostName);
        newContents = doReplace(newContents, prototypeHostPort, hostPort);
        return newContents;
    }

    private String createEntryDeclarations(final List<EntryConfig> endpointConfigs) {
        return endpointConfigs.stream()
                .map(c -> entry(c))
                .collect(Collectors.joining("\n                "));
    }

    private String createEndpointDeclarations(final List<EntryConfig> endpointConfigs) {
        return endpointConfigs.stream()
                .map(s -> endpoint(s))
                .collect(Collectors.joining("\n    "));
    }

    private String endpoint(final EntryConfig c) {
        return String.format("private static final String %sEndpoint = \"%s\";", c.endpointName, c.endpointName);
    }

    private String entry(final EntryConfig c) {
        return String.format(".put(%sEndpoint, new IOType<>(new TypeToken<Request<%s>>() {}, new TypeToken<Response<%s>>() {}))",
                c.endpointName,
                inputClass(c),
                outputClass(c));
    }
}
