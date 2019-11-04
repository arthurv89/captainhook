package com.swipecrowd.captainhook.framework.generation.clientlib;

import com.swipecrowd.captainhook.framework.generation.DefaultReplacer;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import static com.swipecrowd.captainhook.framework.generation.Generator.TEMPLATE_SERVICE_NAME;

public class JavascriptClientReplacer extends DefaultReplacer {
    @Override
    public String replace(final String contents,
                          final String basePackage,
                          final String serviceName,
                          final Set<String> activities,
                          final Properties properties) {
        final EntryConfig entryPrototype = createTemplateEntryConfig();
        final String prototypeHost = "[host]";
        final String prototypePort = "[port]";
        final String prototypeServiceMethod = serviceMethod(entryPrototype);

        final List<EntryConfig> endpointConfigs = getEntryConfigs(basePackage, serviceName, activities);
        final String newServiceMethod = getServiceMethodDeclarations(endpointConfigs);
        final String newHost = properties.getProperty("server.host", "10.0.2.2");
        final String newPort = properties.getProperty("server.port", "8080");

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
