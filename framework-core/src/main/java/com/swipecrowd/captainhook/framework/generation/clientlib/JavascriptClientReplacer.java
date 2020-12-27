package com.swipecrowd.captainhook.framework.generation.clientlib;

import com.swipecrowd.captainhook.framework.generation.DefaultReplacer;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JavascriptClientReplacer extends DefaultReplacer {
    @Override
    public String replace(final String contents,
                          final String servicePackage,
                          final String serviceName,
                          final Set<String> activities) {
        final EntryConfig entryPrototype = createTemplateEntryConfig();
        final String prototypeServiceMethod = serviceMethod(entryPrototype);

        final List<EntryConfig> endpointConfigs = getEntryConfigs(servicePackage, serviceName, activities);
        final String newServiceMethod = getServiceMethodDeclarations(endpointConfigs);

        String newContents = contents;
        newContents = doReplace(newContents, "Service__", upperFirst(serviceName));
        newContents = doReplace(newContents, prototypeServiceMethod, newServiceMethod);
        return newContents;
    }

    @Override
    public File createRenamedFile(final String serviceName, final File file) {
        return new File(file.getParentFile(), serviceName + file.getName());
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
