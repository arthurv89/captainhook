package com.swipecrowd.captainhook.framework.application.server;

import com.swipecrowd.captainhook.framework.application.common.AbstractActivity;
import com.swipecrowd.captainhook.framework.application.common.AbstractRequestContext;
import com.swipecrowd.captainhook.framework.application.common.Input;
import com.swipecrowd.captainhook.framework.application.common.Output;

public interface AbstractServerEndpointComponent {
    <I extends Input, O extends Output, RC extends AbstractRequestContext> ServerActivityConfig<I, O, RC> get(final String activityName);
    <I extends Input, O extends Output, RC extends AbstractRequestContext> void registerActivity(final AbstractActivity<I, O, RC> activity);
    AbstractGeneratedServerProperties getGeneratedServerProperties();
    AbstractServerProperties getServerProperties();
}
