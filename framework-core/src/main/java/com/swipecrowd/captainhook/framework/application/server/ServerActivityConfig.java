package com.swipecrowd.captainhook.framework.application.server;

import com.swipecrowd.captainhook.framework.application.common.AbstractActivity;
import com.swipecrowd.captainhook.framework.application.common.AbstractRequestContext;
import com.swipecrowd.captainhook.framework.application.common.IOType;
import com.swipecrowd.captainhook.framework.application.common.Input;
import com.swipecrowd.captainhook.framework.application.common.Output;
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
