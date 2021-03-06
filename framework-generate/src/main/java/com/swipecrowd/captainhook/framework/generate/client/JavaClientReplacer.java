package com.swipecrowd.captainhook.framework.generate.client;

import com.swipecrowd.captainhook.framework.generate.common.AbstractReplacer;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JavaClientReplacer extends AbstractReplacer {
    @Override
    public String replace(final String contents,
                          final String servicePackage,
                          final String serviceName,
                          final Set<String> activities) {
        final EntryConfig entryPrototype = createTemplateEntryConfig();
        final String prototypeServiceMethod = serviceMethod(entryPrototype);
        final String prototypePackage = entryPrototype.getServicePackage();

        final List<EntryConfig> endpointConfigs = getEntryConfigs(servicePackage, serviceName, activities);
        final String newServiceMethod = getServiceMethodDeclarations(endpointConfigs);

        String newContents = contents;
        newContents = doReplace(newContents, prototypeServiceMethod, newServiceMethod);
        newContents = doReplace(newContents, prototypePackage, servicePackage);
        return newContents;
    }

    @Override
    public File createRenamedFile(final String serviceName, final File file) {
        return new File(file.getParentFile(), serviceName + file.getName());
    }

    private String getServiceMethodDeclarations(final List<EntryConfig> endpointConfigs) {
        return endpointConfigs.stream()
                .map(c -> serviceMethod(c))
                .collect(Collectors.joining("\n\n    "));
    }

    private String serviceMethod(final EntryConfig c) {
        return String.format(
"    public Observable<%s> %sCall(final %s input) {\n" +
"        return createCall(\"%s\", \"%s\", input, new TypeToken<Response<%s>>() {});\n" +
"    }",
                outputClass(c),
                lowerFirst(c.endpointName),
                inputClass(c),
                upperFirst(c.getServiceName()),
                c.endpointName,
                outputClass(c));
    }
}
