package com.swipecrowd.captainhook.test.testservice;

import com.swipecrowd.captainhook.framework.application.server.AbstractServerProperties;
import com.swipecrowd.captainhook.framework.application.server.ApplicationArguments;

public class TestServiceServerProperties extends AbstractServerProperties {
    public static final String DESTROY_KEY = "DESTROY";

    public TestServiceServerProperties(final ApplicationArguments applicationArguments) {
        super(applicationArguments);
    }
}
