package nl.arthurvlug.captainhook.exampleservice2;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceConfiguration {
    public static final String name = "ExampleService 2";
    public static final String baseUrl = "http://localhost:8081";
    public static final String PACKAGE_NAME = "nl.arthurvlug.captainhook.exampleservice2";
}
