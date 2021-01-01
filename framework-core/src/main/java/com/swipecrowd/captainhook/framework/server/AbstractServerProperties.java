package com.swipecrowd.captainhook.framework.server;

import lombok.Getter;

import java.util.Optional;

import static com.swipecrowd.captainhook.framework.server.ApplicationArguments.PORT_KEY;
import static com.swipecrowd.captainhook.framework.server.PropertiesUtils.getOrThrow;

public abstract class AbstractServerProperties {
    @Getter private final String stage;
    @Getter private final String region;
    @Getter private final ApplicationArguments applicationArguments;
    @Getter private final int port;
    @Getter private final String name;

    @Getter private final Optional<Integer> maxAttempts;
    @Getter private final Optional<Integer> waitDurationMs;
    @Getter private final Optional<Boolean> writableStackTraceEnabled;
    @Getter private final Optional<Integer> maxConcurrentCalls;
    @Getter private final Optional<Integer> limitForPeriod;
    @Getter private final Optional<Boolean> fairCallHandlingStrategyEnabled;
    @Getter private final Optional<Integer> maxWaitDuration;
    @Getter private final Optional<Integer> timeDuration;
    @Getter private final Optional<Integer> limitRefreshPeriod;

    public AbstractServerProperties(final ApplicationArguments applicationArguments) {
        this.applicationArguments = applicationArguments;
        this.stage = getOrThrow("stage", applicationArguments);
        this.region = getOrThrow("region", applicationArguments);

        this.name = PropertiesUtils.get("name", stage, region, applicationArguments);
        this.port = Integer.parseInt(PropertiesUtils.get(PORT_KEY, stage, region, applicationArguments));

        this.maxAttempts = getInt("maxAttempts");
        this.waitDurationMs = getInt("waitDurationMs");
        this.writableStackTraceEnabled = getBool("writableStackTraceEnabled");
        this.maxConcurrentCalls = getInt("maxConcurrentCalls");
        this.limitForPeriod = getInt("limitForPeriod");
        this.fairCallHandlingStrategyEnabled = getBool("fairCallHandlingStrategyEnabled");
        this.maxWaitDuration = getInt("maxWaitDuration");
        this.timeDuration = getInt("timeDuration");
        this.limitRefreshPeriod = getInt("limitRefreshPeriod");
    }

    private Optional<Boolean> getBool(final String key) {
        return applicationArguments.get(key).map(x -> Boolean.valueOf(x));
    }

    private Optional<Integer> getInt(final String key) {
        return applicationArguments.get(key).map(x -> Integer.parseInt(x));
    }
}
