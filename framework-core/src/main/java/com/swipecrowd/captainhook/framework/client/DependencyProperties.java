package com.swipecrowd.captainhook.framework.client;

import com.swipecrowd.captainhook.framework.server.AbstractServerProperties;
import com.swipecrowd.captainhook.framework.server.ApplicationArguments;
import com.swipecrowd.captainhook.framework.server.PropertiesUtils;
import lombok.Getter;

import static com.swipecrowd.captainhook.framework.server.ApplicationArguments.PORT_KEY;
import static com.swipecrowd.captainhook.framework.server.PropertiesUtils.getOrThrow;

public class DependencyProperties {
    @Getter private final String stage;
    @Getter private final String region;
    @Getter private final String serviceName;
    private final ApplicationArguments applicationArguments;

    @Getter private final String host;
    @Getter private final int port;

    DependencyProperties(final AbstractServerProperties serverProperties,
                         final String serviceName) {
        this.applicationArguments = serverProperties.getApplicationArguments();
        this.serviceName = serviceName;

        this.stage = getOrThrow("stage", applicationArguments);
        this.region = getOrThrow("region", applicationArguments);
        this.host = getDependencyArgument("server.host");
        this.port = Integer.parseInt(getDependencyArgument(PORT_KEY));
    }

    protected String getDependencyArgument(final String key) {
        return PropertiesUtils.get(serviceName + "." + key, stage, region, applicationArguments);
    }
}
