package com.swipecrowd.captainhook.framework.server;

import lombok.Getter;
import lombok.ToString;

import static com.swipecrowd.captainhook.framework.server.ApplicationArguments.PORT_KEY;
import static com.swipecrowd.captainhook.framework.server.PropertiesUtils.getOrThrow;

@ToString
public abstract class AbstractServerProperties {
    @Getter private final String stage;
    @Getter private final String region;
    @Getter private final ApplicationArguments applicationArguments;
    @Getter private final int port;
    @Getter private final String name;

    public AbstractServerProperties(final ApplicationArguments applicationArguments) {
        this.applicationArguments = applicationArguments;
        this.stage = getOrThrow("stage", applicationArguments);
        this.region = getOrThrow("region", applicationArguments);

        this.name = PropertiesUtils.get("name", stage, region, applicationArguments);
        this.port = Integer.parseInt(PropertiesUtils.get(PORT_KEY, stage, region, applicationArguments));
    }
}
