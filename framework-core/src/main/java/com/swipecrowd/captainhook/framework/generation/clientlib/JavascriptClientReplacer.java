package com.swipecrowd.captainhook.framework.generation.clientlib;

import com.swipecrowd.captainhook.framework.generation.DefaultReplacer;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JavascriptClientReplacer extends DefaultReplacer {
    @Override
    public String replace(final String contents,
                          final String basePackage,
                          final String serviceName,
                          final Set<String> activities,
                          final String newHost,
                          final String newPort) {
        final EntryConfig entryPrototype = createTemplateEntryConfig();
        final String prototypeHost = "[host]";
        final String prototypePort = "[port]";
        final String prototypeServiceMethod = serviceMethod(entryPrototype);

        final List<EntryConfig> endpointConfigs = getEntryConfigs(basePackage, serviceName, activities);
        final String newServiceMethod = getServiceMethodDeclarations(endpointConfigs);

        String newContents = contents;
        newContents = doReplace(newContents, prototypeHost, newHost);
        newContents = doReplace(newContents, prototypePort, newPort);
        newContents = doReplace(newContents, "_Service", upperFirst(serviceName));
        newContents = doReplace(newContents, prototypeServiceMethod, newServiceMethod);
        return newContents;
    }

    private String getServiceMethodDeclarations(final List<EntryConfig> endpointConfigs) {
        return endpointConfigs.stream()
                .map(c -> serviceMethod(c))
                .collect(Collectors.joining("\n"));
    }

    private String serviceMethod(final EntryConfig c) {
        return String.format("" +
                "        get%sCall: function(input) {\n" +
                "            return this.createCall(\"%s\", input);\n" +
                "        },",
                c.endpointName,
                c.endpointName);
    }
}
