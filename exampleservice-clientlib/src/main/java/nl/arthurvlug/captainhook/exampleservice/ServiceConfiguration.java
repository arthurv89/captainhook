package nl.arthurvlug.captainhook.exampleservice;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceConfiguration {
    public static final String name = "ExampleService 1";
    public static final String baseUrl = "http://localhost:8080";
    public static final String PACKAGE_NAME = "nl.arthurvlug.captainhook.exampleservice";
}
