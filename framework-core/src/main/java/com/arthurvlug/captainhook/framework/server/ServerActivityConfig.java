package com.arthurvlug.captainhook.framework.server;

import com.google.gson.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import com.arthurvlug.captainhook.framework.common.response.Output;

@Value
@Getter(AccessLevel.PACKAGE)
class ServerActivityConfig<I extends Input, O extends Output, RC extends AbstractRequestContext> {
    private AbstractActivity<I, O, RC> activity;
    private final IOType<I, O> ioType;
}
