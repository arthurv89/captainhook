package com.arthurvlug.captainhook.examplemiddleservice;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceConfiguration {
    public static final String name = "Middle Service";
    // TODO: Refactor the 2 below
    public static final String baseUrl = "http://localhost:8082";
    public static final String PACKAGE_NAME = "com.arthurvlug.captainhook.examplemiddleservice";
}
