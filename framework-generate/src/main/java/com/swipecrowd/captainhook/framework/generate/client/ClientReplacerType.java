package com.swipecrowd.captainhook.framework.generate.client;

import com.google.common.collect.ImmutableMap;
import com.swipecrowd.captainhook.framework.generate.common.Replacer;
import com.swipecrowd.captainhook.framework.generate.common.ReplacerType;

import java.util.Map;

public class ClientReplacerType extends ReplacerType {
    private static final Map<String, Replacer> map = ImmutableMap.<String, Replacer>builder()
            .put("JavascriptClient.js", new JavascriptClientReplacer())
            .put("JavaClient.java", new JavaClientReplacer())
            .build();

    @Override
    public Replacer fromFileName(final String name) {
        System.out.println("Replacer map: " + map);
        return map.get(name);
    }
}
