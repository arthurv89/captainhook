package com.arthurvlug._service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceConfiguration {
    public static final String baseUrl = "http://localhost:8081";
    public static final String PACKAGE_NAME = "nl.arthurvlug.captainhook.exampleservice2";
}
