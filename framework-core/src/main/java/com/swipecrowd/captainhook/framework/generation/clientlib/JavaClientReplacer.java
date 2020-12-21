package com.swipecrowd.captainhook.framework.generation.clientlib;

import com.swipecrowd.captainhook.framework.generation.DefaultReplacer;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.swipecrowd.captainhook.framework.generation.Generator.getPackage;

public class JavaClientReplacer extends DefaultReplacer {
    @Override
    public String replace(final String contents,
                          final String basePackage,
                          final String serviceName,
                          final Set<String> activities,
                          final String newHost,
                          final String newPort) {
        final EntryConfig entryPrototype = createTemplateEntryConfig();
        final String prototypeServiceMethod = serviceMethod(entryPrototype);
        final String prototypePackage = entryPrototype.getPackage();
        final String prototypeHost = "[host]";
        final String prototypePort = "[port]";

        final List<EntryConfig> endpointConfigs = getEntryConfigs(basePackage, serviceName, activities);
        final String newServiceMethod = getServiceMethodDeclarations(endpointConfigs);
        final String newPackage = getPackage(basePackage, serviceName);

        String newContents = contents;
        newContents = doReplace(newContents, prototypeServiceMethod, newServiceMethod);
        newContents = doReplace(newContents, prototypePackage, newPackage);
        newContents = doReplace(newContents, prototypePort, newPort);
        newContents = doReplace(newContents, prototypeHost, newHost);
        return newContents;
    }

    private String getServiceMethodDeclarations(final List<EntryConfig> endpointConfigs) {
        return endpointConfigs.stream()
                .map(c -> serviceMethod(c))
                .collect(Collectors.joining("\n\n    "));
    }

    private String serviceMethod(final EntryConfig c) {
        return String.format(
"    public Observable<%s> %sCall(final %s input) {\n" +
"        return createCall(\"%s\", input, new TypeToken<Response<%s>>() {});\n" +
"    }",
                outputClass(c),
                lowerFirst(c.endpointName),
                inputClass(c),
                c.endpointName,
                outputClass(c));
    }
}
