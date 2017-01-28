package nl.arthurvlug.captainhook.exampleservice;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.arthurvlug.captainhook.framework.client.AbstractClientConfiguration;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceConfiguration {
    public static final String baseUrl = "http://localhost:8080";
    public static final String PACKAGE_NAME = "nl.arthurvlug.captainhook.exampleservice";
}
