package com.swipecrowd.captainhook.framework.server;

import com.swipecrowd.captainhook.framework.common.response.Output;

public interface AbstractServerEndpointComponent {
    <I extends Input, O extends Output, RC extends AbstractRequestContext> ServerActivityConfig<I, O, RC> get(final String activityName);
    <I extends Input, O extends Output, RC extends AbstractRequestContext> void registerActivity(final AbstractActivity<I, O, RC> activity);
    AbstractServerProperties getServerProperties();
}
