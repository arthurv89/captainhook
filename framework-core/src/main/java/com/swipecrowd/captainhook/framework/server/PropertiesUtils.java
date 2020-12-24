package com.swipecrowd.captainhook.framework.server;

import java.util.Map;
import java.util.Optional;

public class PropertiesUtils {
    public static String get(final String key,
                             String stage,
                             String region,
                             Map<String, String> map) {
        assert stage != null;
        assert region != null;

        String fullKey = String.format("%s.%s.%s", stage, region, key);
        String noRegionKey = String.format("%s.%s.%s", stage, "*", key);
        String noStageKey = String.format("%s.%s.%s", "*", region, key);
        String noStageAndNoRegionKey = String.format("%s.%s.%s", "*", "*", key);
        return Optional.ofNullable(map.get(fullKey))
                .or(() -> Optional.ofNullable(map.get(noRegionKey)))
                .or(() -> Optional.ofNullable(map.get(noStageKey)))
                .or(() -> Optional.ofNullable(map.get(noStageAndNoRegionKey)))
                .orElseThrow(() -> new RuntimeException("Could not find value for key " + fullKey));
    }

    public static String getOrThrow(final String key,
                                    Map<String, String> map) {
        return Optional.ofNullable(map.get(key))
                .orElseThrow(() -> new RuntimeException(String.format("Could not find key %s in properties", key)));
    }
}
