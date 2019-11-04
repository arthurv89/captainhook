package com.swipecrowd.captainhook.framework.generation;

import com.google.common.collect.ImmutableMap;
import com.swipecrowd.captainhook.framework.generation.clientlib.JavaClientReplacer;
import com.swipecrowd.captainhook.framework.generation.clientlib.JavascriptClientReplacer;
import com.swipecrowd.captainhook.framework.generation.server.ServerPropertiesReplacer;

import java.util.Map;

public enum ReplacerType {
    ;

    private static Map<String, Replacer> map = ImmutableMap.<String, Replacer>builder()
            .put("JavascriptClient.js", new JavascriptClientReplacer())
            .put("JavaClient.java", new JavaClientReplacer())
            .put("ServerProperties.java", new ServerPropertiesReplacer())
            .build();

    public static Replacer fromFileName(final String name) {
        System.out.println(map);
        final Replacer replacer = map.get(name);
        final ServerPropertiesReplacer x = new ServerPropertiesReplacer();
        return replacer;
    }
}
