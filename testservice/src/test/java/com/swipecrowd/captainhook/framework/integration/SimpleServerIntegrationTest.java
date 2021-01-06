package com.swipecrowd.captainhook.framework.integration;

import com.swipecrowd.captainhook.test.testservice.TestServiceServerProperties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Optional;

import static com.swipecrowd.captainhook.framework.integration.IntegrationTestUtils.JAVA_INDEX_URL;
import static com.swipecrowd.captainhook.framework.integration.IntegrationTestUtils.JAVA_PORT;
import static com.swipecrowd.captainhook.framework.integration.IntegrationTestUtils.UNUSED_PORT_INDEX_URL;
import static com.swipecrowd.captainhook.framework.integration.IntegrationTestUtils.getTestServiceServerProperties;
import static com.swipecrowd.captainhook.framework.integration.IntegrationTestUtils.startApplication;
import static com.swipecrowd.captainhook.framework.integration.IntegrationTestUtils.verifyProperties;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimpleServerIntegrationTest {
    private static ConfigurableApplicationContext context;

    @Timeout(60)
    @BeforeAll
    static void beforeAll() {
        context = startApplication(JAVA_PORT, Optional.empty());
    }

    @AfterAll
    static void afterAll() {
        if(context != null) {
            context.close();
        }
    }

    @Test
    public void testStatus() throws IOException, InterruptedException {
        final TestServiceServerProperties testServiceServerProperties = getTestServiceServerProperties(JAVA_INDEX_URL);
        assertThat(testServiceServerProperties.getPort()).isEqualTo(JAVA_PORT);
    }

    @Test
    public void testNoStatus() {
        assertThrows(ConnectException.class, () -> {
            getTestServiceServerProperties(UNUSED_PORT_INDEX_URL);
        });
    }

    @Test
    public void testShowConfigOnJavaApplication() throws IOException, InterruptedException {
        final TestServiceServerProperties testServiceServerProperties = getTestServiceServerProperties(JAVA_INDEX_URL);

        verifyProperties(JAVA_PORT, testServiceServerProperties);
    }
}
