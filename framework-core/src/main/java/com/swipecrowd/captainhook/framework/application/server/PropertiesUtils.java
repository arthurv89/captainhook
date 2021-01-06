package com.swipecrowd.captainhook.framework.application.server;

import static com.swipecrowd.captainhook.framework.application.server.ApplicationArguments.PORT_KEY;

public class PropertiesUtils {
    public static String get(final String key,
                             String stage,
                             String region,
                             ApplicationArguments applicationArguments) {
        assert stage != null;
        assert region != null;

        String fullKey = String.format("%s.%s.%s", stage, region, key);
        String noRegionKey = String.format("%s.%s.%s", stage, "*", key);
        String noStageKey = String.format("%s.%s.%s", "*", region, key);
        String noStageAndNoRegionKey = String.format("%s.%s.%s", "*", "*", key);
        return applicationArguments.get(fullKey)
                .or(() -> applicationArguments.get(noRegionKey))
                .or(() -> applicationArguments.get(noStageKey))
                .or(() -> applicationArguments.get(noStageAndNoRegionKey))
                .orElseThrow(() -> new RuntimeException("Could not find value for key " + fullKey + " MAP: " + applicationArguments));
    }

    public static String getOrThrow(final String key,
                                    final ApplicationArguments applicationArguments) {
        return applicationArguments.get(key)
                .orElseThrow(() -> new RuntimeException(String.format("Could not find key %s in properties", key)));
    }

    public static String getPort(final ApplicationArguments applicationArguments) {
        final String stage = getOrThrow("stage", applicationArguments);
        final String region = getOrThrow("region", applicationArguments);

        return get(PORT_KEY, stage, region, applicationArguments);
    }
}
