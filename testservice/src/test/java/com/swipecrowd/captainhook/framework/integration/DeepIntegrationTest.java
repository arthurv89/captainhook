package com.swipecrowd.captainhook.framework.integration;

import com.swipecrowd.captainhook.framework.common.response.Response;
import com.swipecrowd.captainhook.test.testservice.ServiceMain;
import com.swipecrowd.captainhook.test.testservice.TestServiceServerProperties;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldInput;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldOutput;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.swipecrowd.captainhook.framework.integration.IntegrationTestUtils.CAPTAIN_HOOK;
import static com.swipecrowd.captainhook.framework.integration.IntegrationTestUtils.HOSTNAME;
import static com.swipecrowd.captainhook.framework.integration.IntegrationTestUtils.JAVA_INDEX_URL;
import static com.swipecrowd.captainhook.framework.integration.IntegrationTestUtils.JAVA_PORT;
import static com.swipecrowd.captainhook.framework.integration.IntegrationTestUtils.getJsonResponse;
import static com.swipecrowd.captainhook.framework.integration.IntegrationTestUtils.getTestServiceServerProperties;
import static com.swipecrowd.captainhook.framework.integration.IntegrationTestUtils.standardArgs;
import static com.swipecrowd.captainhook.framework.integration.IntegrationTestUtils.startApplication;
import static com.swipecrowd.captainhook.framework.integration.IntegrationTestUtils.verifyProperties;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeepIntegrationTest {
    private static Process process;
    private static ConfigurableApplicationContext standardApplicationContext;

    public static final int BASH_PORT = 8001;
    public static final String BASH_INDEX_URL = "http://localhost:" + BASH_PORT;
    final ExecutorService executorService = Executors.newFixedThreadPool(100);

    @BeforeAll
    @Timeout(60)
    public static void beforeAll() throws IOException, InterruptedException {
        try {
            final TestServiceServerProperties testServiceServerProperties = getTestServiceServerProperties(BASH_INDEX_URL);
            throw new IllegalStateException("There is already a process running on port " + BASH_PORT +". Can't run tests");
        } catch (ConnectException ignored) {}

        startApplicationOnDefaultPort();
        startProcessOutputReader(process.getInputStream(), System.out);
        startProcessOutputReader(process.getErrorStream(), System.err);
        waitForBashApplication();
    }

    @Test
    public void testStatus() throws IOException, InterruptedException {
        testServiceUp(BASH_INDEX_URL, BASH_PORT);
    }

    @Test
    public void testJavaApplication_showConfig() throws IOException, InterruptedException {
        try(ConfigurableApplicationContext context = startApplication(JAVA_PORT, Optional.of(BASH_PORT))) {
            TestServiceServerProperties testServiceServerProperties = getTestServiceServerProperties(JAVA_INDEX_URL);

            verifyProperties(JAVA_PORT, testServiceServerProperties);
            verifyDependencyProperties(testServiceServerProperties);

            assertThat(testServiceServerProperties.getApplicationArguments().get("*.*.testProperty")).hasValue("testPropertyValue");
            assertThat(testServiceServerProperties.getApplicationArguments().get("*.*.javaStartApplicationArgument")).hasValue("javaStartApplicationArgumentValue");

        }
    }

    @Test
    public void testBashApplication_showConfig() throws IOException, InterruptedException {
        try(ConfigurableApplicationContext context = startApplication(JAVA_PORT, Optional.of(BASH_PORT))) {
            TestServiceServerProperties testServiceServerProperties = getTestServiceServerProperties(BASH_INDEX_URL);

            verifyProperties(BASH_PORT, testServiceServerProperties);
            assertThat(testServiceServerProperties.getApplicationArguments().get("*.*.commandLineArgument")).hasValue("commandLineArgumentValue");
            assertThat(testServiceServerProperties.getApplicationArguments().get("*.*.clientlibProperty")).hasValue("clientlibPropertyValue");
        }
    }

    @Test
    public void testBashApplication_depth1() throws IOException, InterruptedException {
        final Response<HelloWorldOutput> response = getJsonResponse(
                createInput(0),
                BASH_INDEX_URL);

        assertThat(response.getValue().getMessage()).isEqualTo(BASH_PORT + " -> Received name: " + CAPTAIN_HOOK);
        assertThat(response.getValue().getRespondingTime()).isNotNull();
    }

    @Test
    public void testJavaApplication_depth1() throws IOException, InterruptedException {
        testJavaServiceNotUp();

        try(ConfigurableApplicationContext ignored = startApplication(JAVA_PORT, Optional.of(BASH_PORT))) {
            testServiceUp(JAVA_INDEX_URL, JAVA_PORT);

            final Response<HelloWorldOutput> response = getJsonResponse(
                    createInput(0),
                    JAVA_INDEX_URL);

            assertThat(response.getValue().getMessage()).isEqualTo(JAVA_PORT + " -> Received name: " + CAPTAIN_HOOK);
            assertThat(response.getValue().getRespondingTime()).isNotNull();
        }
    }

    @Test
    public void testJavaApplication_depth2() throws IOException, InterruptedException {
        testJavaServiceNotUp();

        try(ConfigurableApplicationContext ignored = startApplication(JAVA_PORT, Optional.of(BASH_PORT))) {
            testServiceUp(JAVA_INDEX_URL, JAVA_PORT);
            testServiceUp(BASH_INDEX_URL, BASH_PORT);

            final Response<HelloWorldOutput> response = getJsonResponse(
                    createInput(1),
                    JAVA_INDEX_URL);

            assertThat(response.getValue().getMessage()).isEqualTo(BASH_PORT + " -> Received name: " + CAPTAIN_HOOK);
            assertThat(response.getValue().getRespondingTime()).isNotNull();
        }
    }

    @Test
    public void testJavaApplication_depth3() throws IOException, InterruptedException {
        testJavaServiceNotUp();

        try(ConfigurableApplicationContext ignored = startApplication(JAVA_PORT, Optional.of(BASH_PORT))) {
            testServiceUp(JAVA_INDEX_URL, JAVA_PORT);

            final HelloWorldInput input = createInput(2);

            final Response<HelloWorldOutput> response = getJsonResponse(input, JAVA_INDEX_URL);
            assertThat(response.getValue().getMessage()).startsWith("Recovered from a failure: The source did not signal an event for 1000 milliseconds and has been terminated");
        }
    }

    @Test
    public void testJavaApplication_depth3_withHighMaxConcurrentCalls_doesNotCauseRateLimit() throws IOException, InterruptedException, ExecutionException {
        depth3(0,
                100,
                0);
    }

    @Test
    public void testJavaApplication_depth3_causeRateLimitExceededException() throws IOException, InterruptedException, ExecutionException {
        depth3(0,
                70,
                30,
                "--*.*.maxConcurrentCalls=1", "--*.*.cancelRunningFuture=True", "--*.*.timeoutDuration=100");
    }

    private void depth3(final int expectedErrors,
                        final int expectedGood,
                        final int expectedCached,
                        final String... extraArgs) throws IOException, InterruptedException, ExecutionException {
        testJavaServiceNotUp();

        try(ConfigurableApplicationContext ignored = ServiceMain.startApplication(standardArgs(JAVA_PORT, Optional.of(BASH_PORT), extraArgs))) {
            testServiceUp(JAVA_INDEX_URL, JAVA_PORT);

            int good = 0;
            int error = 0;
            int cached = 0;

            for (int j = 0; j < 10; j++) {
                final List<Future<Response<HelloWorldOutput>>> tasks = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    final Future<Response<HelloWorldOutput>> task = executorService.submit(() -> getJsonResponse(createInput(1), JAVA_INDEX_URL));
                    tasks.add(task);
                }

                for (Future<Response<HelloWorldOutput>> task : tasks) {
                    final Response<HelloWorldOutput> response = task.get();
                    final String message = response.getValue().getMessage();
                    if (message.startsWith("Recovered from a failure: ")) {
                        error++;
                    } else if (message.startsWith("CACHED ")) {
                        cached++;
                    } else {
                        good++;
                    }
                    System.out.println(message);
                }
            }
            assertThat(error).isGreaterThanOrEqualTo(expectedErrors);
            assertThat(good).isLessThanOrEqualTo(expectedGood);
            assertThat(cached).isGreaterThanOrEqualTo(expectedCached);
        }
    }

    @AfterAll
    public static void afterAll() throws IOException, InterruptedException {
        final HelloWorldInput input = HelloWorldInput.builder()
                .name(TestServiceServerProperties.DESTROY_KEY)
                .build();
        final Response<HelloWorldOutput> response = getJsonResponse(input, BASH_INDEX_URL);
        final String responseMessage = response.getValue().getMessage();
        System.out.println(responseMessage);

        while(true) {
            System.out.println("Waiting for shut down");
            try {
                final TestServiceServerProperties testServiceServerProperties = getTestServiceServerProperties(BASH_INDEX_URL);
                Thread.sleep(500);
            } catch (ConnectException | FileNotFoundException connectException) {
                System.out.println("Done." + connectException);
                return;
            } catch (Exception exception) {
                System.out.println("Found unknown exception. Ignoring. Could be that the connection is suddenly broken.\n" + exception);
            }
        }
    }


    private static void waitForBashApplication() throws IOException, InterruptedException {
        while(true) {
            System.out.println("Waiting for status");
            try {
                final TestServiceServerProperties testServiceServerProperties = getTestServiceServerProperties(BASH_INDEX_URL);
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

    private HelloWorldInput createInput(final int forward, final int i) {
        return createInput(forward, String.format("%s ---- %d", CAPTAIN_HOOK, i));
    }

    private HelloWorldInput createInput(final int forward) {
        return createInput(forward, CAPTAIN_HOOK);
    }

    private HelloWorldInput createInput(final int forward, final String message) {
        return HelloWorldInput.builder()
                .name(message)
                .forward(forward)
                .build();
    }


    private void testServiceUp(final String indexUrl, final int expectedPort) throws IOException, InterruptedException {
        final TestServiceServerProperties testServiceServerProperties = getTestServiceServerProperties(indexUrl);
        assertThat(testServiceServerProperties.getPort()).isEqualTo(expectedPort);
    }

    private void testJavaServiceNotUp() {
        assertThrows(ConnectException.class, () -> {
            getTestServiceServerProperties(JAVA_INDEX_URL);
        });
    }

    private void verifyDependencyProperties(final TestServiceServerProperties testServiceServerProperties) {
        assertThat(testServiceServerProperties.getApplicationArguments().get("*.*.TestService.server.host")).hasValue(HOSTNAME);
        assertThat(testServiceServerProperties.getApplicationArguments().get("*.*.TestService.server.port")).hasValue(String.valueOf(BASH_PORT));
    }
}
