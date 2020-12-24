package com.swipecrowd.captainhook.framework.client;

import com.swipecrowd.captainhook.framework.server.AbstractServerProperties;
import com.swipecrowd.captainhook.framework.server.PropertiesUtils;
import lombok.Getter;

import java.util.Map;

import static com.swipecrowd.captainhook.framework.server.PropertiesUtils.getOrThrow;

public class DependencyProperties {
    @Getter private final String stage;
    @Getter private final String region;
    @Getter private final String serviceName;
    private final Map<String, String> map;

    @Getter private final String host;
    @Getter private final int port;

    DependencyProperties(final AbstractServerProperties serverProperties,
                         final String serviceName) {
        this.map = serverProperties.getMap();
        this.serviceName = serviceName;

        this.stage = getOrThrow("stage", map);
        this.region = getOrThrow("region", map);
        this.host = get("server.host");
        this.port = Integer.parseInt(get("server.port"));
    }

    protected String get(final String key) {
        return PropertiesUtils.get(serviceName + "." + key, stage, region, map);
    }
}
