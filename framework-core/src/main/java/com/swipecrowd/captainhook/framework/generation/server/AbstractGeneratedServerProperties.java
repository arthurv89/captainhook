package com.swipecrowd.captainhook.framework.generation;

import com.swipecrowd.captainhook.framework.common.response.Output;
import com.swipecrowd.captainhook.framework.server.IOType;
import com.swipecrowd.captainhook.framework.server.Input;

import java.util.Map;

public abstract class AbstractGeneratedServerProperties {
    public abstract String getPackageName();

    public abstract Map<String, IOType<? extends Input, ? extends Output>> getIOTypes();
}
