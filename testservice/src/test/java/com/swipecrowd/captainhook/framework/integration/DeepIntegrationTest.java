package com.swipecrowd.captainhook.framework.integration;

import com.swipecrowd.captainhook.framework.common.response.Response;
import com.swipecrowd.captainhook.test.testservice.TestServiceServerProperties;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldInput;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldOutput;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.*;
import java.net.ConnectException;
import java.util.Optional;

import static com.swipecrowd.captainhook.framework.integration.IntegrationTestUtils.*;
import static com.swipecrowd.captainhook.test.testservice.TestServiceServerProperties.SHOW_CONFIG_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeepIntegrationTest {
    private static Process process;
    private static ConfigurableApplicationContext standardApplicationContext;

    public static final int BASH_PORT = 8001;
    public static final String BASH_INDEX_URL = "http://localhost:" + BASH_PORT;

    @BeforeAll
    public static void beforeAll() throws IOException, InterruptedException {
        try {
            final String urlContents = getUrlContents(BASH_INDEX_URL);
            throw new IllegalStateException("There is already a process running on port " + BASH_PORT +". Can't run tests");
        } catch (IOException ignored) {}

        startApplicationOnDefaultPort();
        startProcessOutputReader(process.getInputStream(), System.out);
        startProcessOutputReader(process.getErrorStream(), System.err);
        waitForBashApplication();
    }

    private static void waitForBashApplication() throws IOException, InterruptedException {
        while(true) {
            System.out.println("Waiting for status");
            try {
                final String urlContents = getUrlContents(BASH_INDEX_URL);
                System.out.println("Before done.");
                return;
            } catch (ConnectException connectException) {
                Thread.sleep(500);
            }
        }
    }

    private static void startProcessOutputReader(final InputStream inputStream, final PrintStream printStream) throws IOException {
        new Thread(() -> {
            try (BufferedReader input = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;

                while ((line = input.readLine()) != null) {
                    printStream.println(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private static void startApplicationOnDefaultPort() throws InterruptedException {
        new Thread(() -> {
            try {
                process = Runtime.getRuntime().exec("bash start-server.sh");
            } catch (Exception e) {
                throw new RuntimeException("Failed to start up application!");
            }
        }).start();

        while(process == null) {
            Thread.sleep(10);
        }
    }

    @AfterAll
    public static void afterAll() throws IOException {
        final HelloWorldInput input = HelloWorldInput.builder()
                .name(TestServiceServerProperties.DESTROY_KEY)
                .build();
        final Response<HelloWorldOutput> response = getJsonResponse(input, BASH_INDEX_URL);
        final String responseMessage = response.getValue().getMessage();
        System.out.println(responseMessage);

        while(true) {
            System.out.println("Waiting for shut down");
            try {
                final String urlContents = getUrlContents(BASH_INDEX_URL);
                Thread.sleep(500);
            } catch (ConnectException connectException) {
                System.out.println("Done." + connectException);
                return;
            } catch (Exception exception) {
                System.out.println("Found unknown exception. Ignoring. Could be that the connection is suddenly broken.\n" + exception);
            }
        }
    }

    @Test
    public void testStatus() throws IOException {
        testServiceUp(BASH_INDEX_URL, BASH_PORT);
    }

    @Test
    public void testJavaApplication_showConfig() throws IOException {
        try(ConfigurableApplicationContext context = startApplication(JAVA_PORT, Optional.of(BASH_PORT))) {
            final HelloWorldInput input = createInput(SHOW_CONFIG_KEY, 0);

            final Response<HelloWorldOutput> response = getJsonResponse(input, JAVA_INDEX_URL);
            final String message = response.getValue().getMessage();
            final TestServiceServerProperties testServiceServerProperties = createGson().fromJson(message, TestServiceServerProperties.class);
            assertThat(testServiceServerProperties.getStage()).isEqualTo(STAGE);
            assertThat(testServiceServerProperties.getRegion()).isEqualTo(REGION);
            assertThat(testServiceServerProperties.getApplicationArguments().get("stage")).hasValue(STAGE);
            assertThat(testServiceServerProperties.getApplicationArguments().get("region")).hasValue(REGION);
            assertThat(testServiceServerProperties.getApplicationArguments().get("*.*.server.port")).hasValue(String.valueOf(JAVA_PORT));
            assertThat(testServiceServerProperties.getApplicationArguments().get("*.*.name")).hasValue(TEST_SERVICE);
            assertThat(testServiceServerProperties.getApplicationArguments().get("*.*.TestService.server.host")).hasValue(HOSTNAME);
            assertThat(testServiceServerProperties.getApplicationArguments().get("*.*.TestService.server.port")).hasValue(String.valueOf(BASH_PORT));
            assertThat(testServiceServerProperties.getPort()).isEqualTo(JAVA_PORT);
            assertThat(testServiceServerProperties.getName()).isEqualTo(TEST_SERVICE);
        }
    }

    @Test
    public void testBashApplication_showConfig() throws IOException {
        try(ConfigurableApplicationContext context = startApplication(JAVA_PORT, Optional.of(BASH_PORT))) {
            final HelloWorldInput input = createInput(SHOW_CONFIG_KEY, 0);

            final Response<HelloWorldOutput> response = getJsonResponse(input, BASH_INDEX_URL);
            final String message = response.getValue().getMessage();
            final TestServiceServerProperties testServiceServerProperties = createGson().fromJson(message, TestServiceServerProperties.class);
            assertThat(testServiceServerProperties.getStage()).isEqualTo(STAGE);
            assertThat(testServiceServerProperties.getRegion()).isEqualTo(REGION);
            assertThat(testServiceServerProperties.getApplicationArguments().get("stage")).hasValue(STAGE);
            assertThat(testServiceServerProperties.getApplicationArguments().get("region")).hasValue(REGION);
            assertThat(testServiceServerProperties.getApplicationArguments().get("*.*.server.port")).hasValue(String.valueOf(BASH_PORT));
            assertThat(testServiceServerProperties.getApplicationArguments().get("*.*.name")).hasValue(TEST_SERVICE);
            assertThat(testServiceServerProperties.getPort()).isEqualTo(BASH_PORT);
            assertThat(testServiceServerProperties.getName()).isEqualTo(TEST_SERVICE);
        }
    }


    @Test
    public void testBashApplication_depth1() throws IOException {
        final HelloWorldInput input = createInput(CAPTAIN_HOOK, 0);

        final Response<HelloWorldOutput> response = getJsonResponse(input, BASH_INDEX_URL);
        assertThat(response.getValue().getMessage()).isEqualTo(BASH_PORT + " -> Received name: " + CAPTAIN_HOOK);
        assertThat(response.getValue().getRespondingTime()).isNotNull();
        assertThat(response.getMetadata()).containsKeys("timeSpent", "startTime", "endTime");
    }

    @Test
    public void testJavaApplication_depth1() throws IOException {
        testJavaServiceNotUp();

        try(ConfigurableApplicationContext ignored = startApplication(JAVA_PORT, Optional.of(BASH_PORT))) {
            testServiceUp(JAVA_INDEX_URL, JAVA_PORT);

            final HelloWorldInput input = createInput(CAPTAIN_HOOK, 0);

            final Response<HelloWorldOutput> response = getJsonResponse(input, JAVA_INDEX_URL);
            assertThat(response.getValue().getMessage()).isEqualTo(JAVA_PORT + " -> Received name: " + CAPTAIN_HOOK);
            assertThat(response.getValue().getRespondingTime()).isNotNull();
            assertThat(response.getMetadata()).containsKeys("timeSpent", "startTime", "endTime");
        }
    }

    @Test
    public void testJavaApplication_depth2() throws IOException {
        testJavaServiceNotUp();

        try(ConfigurableApplicationContext ignored = startApplication(JAVA_PORT, Optional.of(BASH_PORT))) {
            testServiceUp(JAVA_INDEX_URL, JAVA_PORT);
            testServiceUp(BASH_INDEX_URL, BASH_PORT);

            final HelloWorldInput input = createInput(CAPTAIN_HOOK, 1);

            final Response<HelloWorldOutput> response = getJsonResponse(input, JAVA_INDEX_URL);
            assertThat(response.getValue().getMessage()).isEqualTo(BASH_PORT + " -> Received name: " + CAPTAIN_HOOK);
            assertThat(response.getValue().getRespondingTime()).isNotNull();
            assertThat(response.getMetadata()).containsKeys("timeSpent", "startTime", "endTime");
        }
    }

    @Test
    public void testJavaApplication_depth3() throws IOException {
        testJavaServiceNotUp();

        try(ConfigurableApplicationContext ignored = startApplication(JAVA_PORT, Optional.of(BASH_PORT))) {
            testServiceUp(JAVA_INDEX_URL, JAVA_PORT);

            final HelloWorldInput input = createInput(CAPTAIN_HOOK, 2);

            final Response<HelloWorldOutput> response = getJsonResponse(input, JAVA_INDEX_URL);
            assertThat(response.getExceptionResult().convertToThrowable().getCause().getMessage()).startsWith("Activity HelloWorld in server TestService threw an exception");
            assertThat(response.getExceptionResult().convertToThrowable().getCause().getCause().getMessage()).startsWith("Could not find value for key dev.EU.TestService.server.host");
        }
    }

    private HelloWorldInput createInput(final String name, final int forward) {
        return HelloWorldInput.builder()
                .name(name)
                .forward(forward)
                .build();
    }


    private void testServiceUp(final String indexUrl, final int port) throws IOException {
        final String urlContents = getUrlContents(indexUrl);
        assertThat(urlContents).isEqualTo(onlineStatus(port));
    }

    private void testJavaServiceNotUp() {
        assertThrows(ConnectException.class, () -> {
            final String s = getUrlContents(JAVA_INDEX_URL);
            System.out.println(s);
        });
    }

}
