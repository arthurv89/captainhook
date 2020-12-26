package com.swipecrowd.captainhook.test.testservice;

import com.swipecrowd.captainhook.framework.server.AbstractServerProperties;
import com.swipecrowd.captainhook.framework.server.ApplicationArguments;

public class TestServiceServerProperties extends AbstractServerProperties {
    public static final String DESTROY_KEY = "DESTROY";

    public TestServiceServerProperties(final ApplicationArguments applicationArguments) {
        super(applicationArguments);
    }
    
    
}
