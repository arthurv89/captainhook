package com.arthurvlug._service.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceConfiguration {
    public static final String name = "[name]";
    public static final String port = "[port]";
    public static final String baseUrl = "http://localhost:" + port;
    public static final String PACKAGE_NAME = "com.arthurvlug._service";
}
