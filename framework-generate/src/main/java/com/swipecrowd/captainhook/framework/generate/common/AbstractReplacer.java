package com.swipecrowd.captainhook.framework.generate.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.swipecrowd.captainhook.framework.generate.common.Generator.TEMPLATE_BASE_PACKAGE;
import static com.swipecrowd.captainhook.framework.generate.common.Generator.TEMPLATE_ENDPOINT;
import static com.swipecrowd.captainhook.framework.generate.common.Generator.TEMPLATE_SERVICE_NAME;

public abstract class AbstractReplacer implements Replacer {
    protected String doReplace(final String contents, final String from, final String to) {
        final String replacedString = contents.replace(from, to);
        if(contents.equals(replacedString)) {
            throw new RuntimeException(String.format("Nothing changed!\n\nFrom:\n%s\n\nTo:\n%s", from, to));
        }
        return replacedString;
    }

    protected static String outputClass(final EntryConfig c) {
        return String.format("%s.activity.%s.%sOutput", c.getServicePackage(), c.endpointName.toLowerCase(), c.endpointName);
    }

    protected static String inputClass(final EntryConfig c) {
        return String.format("%s.activity.%s.%sInput", c.getServicePackage(), c.endpointName.toLowerCase(), c.endpointName);
    }

    protected String lowerFirst(final String s) {
        char[] c = s.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

    protected String upperFirst(final String s) {
        char[] c = s.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        return new String(c);
    }

    protected EntryConfig createTemplateEntryConfig() {
        return new EntryConfig(
                TEMPLATE_BASE_PACKAGE,
                TEMPLATE_SERVICE_NAME,
                TEMPLATE_ENDPOINT
        );
    }

    @AllArgsConstructor
    public class EntryConfig {
        @Getter
        private final String servicePackage;
        @Getter
        private final String serviceName;
        public final String endpointName;
    }

    protected List<EntryConfig> getEntryConfigs(final String servicePackage, final String serviceName, final Set<String> activities) {
        return activities.stream()
                .map(activity -> new EntryConfig(servicePackage, serviceName, activity))
                .collect(Collectors.toList());
    }


    public abstract String replace(final String contents,
                                   final String servicePackage,
                                   final String serviceName,
                                   final Set<String> activities);
}
