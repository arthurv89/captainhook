package com.swipecrowd.captainhook.framework.generation.clientlib;

import com.swipecrowd.captainhook.framework.generation.Generator;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class GenerateClientLibClasses extends Generator {
    public static void main(String[] args) throws IOException {
        if(args[0].equals("framework-core-clientlib")) {
            // Skip if the framework triggered this.
            return;
        }

        final String serviceName = args[0].replace("-clientlib", "");
        final String basePackage = args[1];

        new GenerateClientLibClasses().run(serviceName, basePackage, "client");
    }

    @Override
    protected String replace(final String contents,
                             final String basePackage,
                             final String serviceName,
                             final Set<String> activities,
                             final Properties properties) {
        final EntryConfig entryPrototype = createTemplateEntryConfig();
        final String prototypeServiceMethod = serviceMethod(entryPrototype);
        final String prototypePackage = entryPrototype.getPackage();
        final String prototypePort = "[port]";

        final List<EntryConfig> endpointConfigs = getEntryConfigs(basePackage, serviceName, activities);
        final String newServiceMethod = getServiceMethodDeclarations(endpointConfigs);
        final String newPackage = getPackage(basePackage, serviceName);
        final String newPort = properties.getProperty("server.port", "8080");

        String newContents = contents;
        newContents = doReplace(newContents, prototypeServiceMethod, newServiceMethod);
        newContents = doReplace(newContents, prototypePackage, newPackage);
        newContents = doReplace(newContents, prototypePort, newPort);
        return newContents;
    }

    private String getServiceMethodDeclarations(final List<EntryConfig> endpointConfigs) {
        return endpointConfigs.stream()
                .map(c -> serviceMethod(c))
                .collect(Collectors.joining("\n\n    "));
    }

    private String serviceMethod(final EntryConfig c) {
        return String.format(
            "public Observable<%s> %sCall(final %s input) {\n" +
            "        return createCall(\"%s\", input, new TypeToken<Response<%s>>() {});\n" +
            "    }",
            outputClass(c),
            lowerFirst(c.endpointName),
            inputClass(c),
            c.endpointName,
            outputClass(c));
    }
}
