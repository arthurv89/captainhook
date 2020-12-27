package com.swipecrowd.captainhook.framework.integration;

import com.swipecrowd.captainhook.framework.common.response.Response;
import com.swipecrowd.captainhook.test.testservice.TestServiceServerProperties;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldInput;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldOutput;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Optional;

import static com.swipecrowd.captainhook.framework.integration.IntegrationTestUtils.*;
import static com.swipecrowd.captainhook.test.testservice.TestServiceServerProperties.SHOW_CONFIG_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimpleServerIntegrationTest {
    private static ConfigurableApplicationContext context;

    @BeforeAll
    static void beforeAll() {
        context = startApplication(JAVA_PORT, Optional.empty());
    }

    @AfterAll
    static void afterAll() {
        context.close();
    }

    @Test
    public void testStatus() throws IOException {
        final String urlContents = getUrlContents(JAVA_INDEX_URL);
        assertThat(urlContents).isEqualTo(onlineStatus(JAVA_PORT));
    }

    @Test
    public void testNoStatus() {
        assertThrows(ConnectException.class, () -> {
            getUrlContents(UNUSED_PORT_INDEX_URL);
        });
    }

    @Test
    public void testShowConfigOnJavaApplication() throws IOException {
        final HelloWorldInput input = HelloWorldInput.builder()
                .name(SHOW_CONFIG_KEY)
                .forward(0)
                .build();

        final Response<HelloWorldOutput> response = getJsonResponse(input, JAVA_INDEX_URL);
        final String message = response.getValue().getMessage();
        final TestServiceServerProperties testServiceServerProperties = createGson().fromJson(message, TestServiceServerProperties.class);
        assertThat(testServiceServerProperties.getStage()).isEqualTo(STAGE);
        assertThat(testServiceServerProperties.getRegion()).isEqualTo(REGION);
        assertThat(testServiceServerProperties.getApplicationArguments().get("stage")).hasValue(STAGE);
        assertThat(testServiceServerProperties.getApplicationArguments().get("region")).hasValue(REGION);
        assertThat(testServiceServerProperties.getApplicationArguments().get("*.*.name")).hasValue(TEST_SERVICE);
        assertThat(testServiceServerProperties.getApplicationArguments().get("*.*.server.port")).hasValue(String.valueOf(JAVA_PORT));
        assertThat(testServiceServerProperties.getPort()).isEqualTo(JAVA_PORT);
        assertThat(testServiceServerProperties.getName()).isEqualTo(TEST_SERVICE);
    }

}
