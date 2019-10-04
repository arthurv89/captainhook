package com.swipecrowd.captainhook.framework.server;

import com.swipecrowd.captainhook.framework.common.response.Output;

import java.util.Map;

public abstract class AbstractServerProperties {
    public abstract String getPackageName();

    public abstract String getServerName();

    public abstract int getPort();

    public abstract Map<String, IOType<? extends Input, ? extends Output>> getIOTypes();
}
