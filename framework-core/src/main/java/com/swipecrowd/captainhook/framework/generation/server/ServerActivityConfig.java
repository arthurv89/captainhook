package com.swipecrowd.captainhook.framework.generation;

import com.swipecrowd.captainhook.framework.common.response.Output;
import com.swipecrowd.captainhook.framework.server.IOType;
import com.swipecrowd.captainhook.framework.server.Input;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

@Value
@Getter(AccessLevel.PUBLIC)
public
class ServerActivityConfig<I extends Input, O extends Output, RC extends AbstractRequestContext> {
    AbstractActivity<I, O, RC> activity;
    IOType<I, O> ioType;
}
