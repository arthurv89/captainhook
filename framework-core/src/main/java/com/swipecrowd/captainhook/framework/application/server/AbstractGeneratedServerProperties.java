package com.swipecrowd.captainhook.framework.application.server;

import com.swipecrowd.captainhook.framework.application.common.IOType;
import com.swipecrowd.captainhook.framework.application.common.Input;
import com.swipecrowd.captainhook.framework.application.common.Output;

import java.util.Map;

public abstract class AbstractGeneratedServerProperties {
    public abstract String getPackageName();

    public abstract Map<String, IOType<? extends Input, ? extends Output>> getIOTypes();
}
