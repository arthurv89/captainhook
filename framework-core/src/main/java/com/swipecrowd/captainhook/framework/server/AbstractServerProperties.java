package com.swipecrowd.captainhook.framework.server;

import lombok.Getter;

import java.util.Map;

import static com.swipecrowd.captainhook.framework.server.PropertiesUtils.get;
import static com.swipecrowd.captainhook.framework.server.PropertiesUtils.getOrThrow;

public abstract class AbstractServerProperties {
    @Getter private final String stage;
    @Getter private final String region;
    @Getter private final Map<String, String> map;
    @Getter private final String host;
    @Getter private final int port;

    public AbstractServerProperties(final Map<String, String> map) {
        this.map = map;
        stage = getOrThrow("stage", map);
        region = getOrThrow("region", map);

        this.host = get("server.host", stage, region, map);
        this.port = Integer.parseInt(get("server.port", stage, region, map));
    }
}
