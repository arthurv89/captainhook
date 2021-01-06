package com.swipecrowd.captainhook.framework.generate.server;

import com.google.common.collect.ImmutableMap;
import com.swipecrowd.captainhook.framework.generate.common.Replacer;
import com.swipecrowd.captainhook.framework.generate.common.ReplacerType;

import java.util.Map;

public class ServerReplacerType extends ReplacerType {
    private static final Map<String, Replacer> map = ImmutableMap.<String, Replacer>builder()
            .put("GeneratedServerProperties.java", new GeneratedServerPropertiesReplacer())
            .build();

    public Replacer fromFileName(final String name) {
        System.out.println("Replacer map: " + map);
        return map.get(name);
    }
}
