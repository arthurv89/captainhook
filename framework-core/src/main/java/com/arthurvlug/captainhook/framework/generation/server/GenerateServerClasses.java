package com.arthurvlug.captainhook.framework.generation.server;

import com.arthurvlug.captainhook.framework.generation.Generator;
import com.google.common.base.Joiner;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class GenerateServerClasses extends Generator {
    public static void main(String[] args) throws IOException {
        if(args[0].equals("framework-core-server")) {
            return;
        }
        final String serviceName = args[0];
        final String basePackage = args[1];

        new GenerateServerClasses().run(serviceName, basePackage, "server");
    }

    @Override
    protected String replace(final String contents,
                             final String basePackage,
                             final String serviceName,
                             final Set<String> activities,
                             final Properties properties) {
        final List<EntryConfig> endpointConfigs = getEntryConfigs(basePackage, serviceName, activities);
        final String newEndpointDeclarations = createEndpointDeclarations(endpointConfigs);
        final String newEntryDeclarations = createEntryDeclarations(endpointConfigs);
        final String newPackage = getPackage(basePackage, serviceName);
        final String newPort = properties.getProperty("server.port", "8080");
        final String newName = properties.getProperty("name", TEMPLATE_SERVICE_NAME);

        final EntryConfig entryPrototype = createTemplateEntryConfig();
        final String prototypeEndpoint = endpoint(entryPrototype);
        final String prototypeEntry = entry(entryPrototype);
        final String prototypePackage = entryPrototype.getPackage();
        final String prototypePort = "[port]";
        final String prototypeName = "[name]";

        String newContents = contents;
        newContents = doReplace(newContents, prototypeEndpoint, newEndpointDeclarations);
        newContents = doReplace(newContents, prototypeEntry, newEntryDeclarations);
        newContents = doReplace(newContents, prototypePackage, newPackage);
        newContents = doReplace(newContents, prototypePort, newPort);
        newContents = doReplace(newContents, prototypeName, newName);
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
        return String.format("public static final String %sEndpoint = \"%s\";", c.endpointName, c.endpointName);
    }

    private String entry(final EntryConfig c) {
        return String.format(".put(%sEndpoint, new IOType<>(new TypeToken<Request<%s>>() {}, new TypeToken<Response<%s>>() {}))",
                c.endpointName,
                inputClass(c),
                outputClass(c));
    }
}
