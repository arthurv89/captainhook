package com.swipecrowd.captainhook.framework.integration;

import com.google.common.base.Charsets;
import com.google.common.collect.ObjectArrays;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import com.swipecrowd.captainhook.framework.common.response.Response;
import com.swipecrowd.captainhook.test.testservice.ServiceMain;
import com.swipecrowd.captainhook.test.testservice.TestServiceServerProperties;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldInput;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldOutput;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static com.swipecrowd.captainhook.test.testservice.TestServiceServerProperties.SHOW_CONFIG_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IntegrationTest {
    public static final int PORT = 8001;
    public static final String INDEX_URL = "http://localhost:" + PORT;

    public static final int ALTERNATIVE_PORT = 8002;
    public static final String ALTERNATIVE_INDEX_URL = "http://localhost:" + ALTERNATIVE_PORT;

    public static final int UNUSED_PORT = 8003;
    public static final String UNUSED_PORT_INDEX_URL = "http://localhost:" + UNUSED_PORT;
    private static Process process;

    private static final Gson GSON = createGson();
    private static final String ENDPOINT_NAME = "HelloWorld";
    private static final String ENCODING = "JSON";

    private static final String CAPTAIN_HOOK = "Captain Hook";
    private static ConfigurableApplicationContext standardApplicationContext;

    @BeforeAll
    public static void beforeAll() throws IOException, InterruptedException {
        try {
            final String urlContents = getUrlContents(INDEX_URL);
            throw new IllegalStateException("There is already a process running on port " + PORT +". Can't run tests");
        } catch (IOException ignored) {}

        startApplicationOnDefaultPort();
        startProcessOutputReader(process.getInputStream(), System.out);
        startProcessOutputReader(process.getErrorStream(), System.err);
        waitForDefaultApplication();
    }

    private static void waitForDefaultApplication() throws IOException, InterruptedException {
        while(true) {
            System.out.println("Waiting for status");
            try {
                final String urlContents = getUrlContents(INDEX_URL);
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
        final Response<HelloWorldOutput> response = getJsonResponse(input, INDEX_URL);
        final String responseMessage = response.getValue().getMessage();
        System.out.println(responseMessage);

        while(true) {
            System.out.println("Waiting for shut down");
            try {
                final String urlContents = getUrlContents(INDEX_URL);
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
        final String urlContents = getUrlContents( INDEX_URL);
        assertThat(urlContents).isEqualTo(onlineStatus(PORT));
    }

    @Test
    public void testNoStatus() {
        assertThrows(ConnectException.class, () -> {
            getUrlContents(UNUSED_PORT_INDEX_URL);
        });
    }

    @Test
    public void testShowConfig() throws IOException {
        try(ConfigurableApplicationContext ignored = startApplication(ALTERNATIVE_PORT)) {
            final HelloWorldInput input = HelloWorldInput.builder()
                    .name(SHOW_CONFIG_KEY)
                    .forward(0)
                    .build();

            final Response<HelloWorldOutput> response = getJsonResponse(input, ALTERNATIVE_INDEX_URL);
            assertThat(response.getValue().getMessage()).isEqualTo("TestServiceServerProperties(super=AbstractServerProperties(stage=dev, region=EU, applicationArguments=ApplicationArguments(map={dev.EU.server.host=localhost, *.*.TestService.server.host=localhost, stage=dev, dev.EU.server.port=8002, *.*.name=TestService, region=EU, *.*.TestService.server.port=8002}), port=8002, name=TestService))");
        }
    }

    @Test
    public void testHelloWorldCallWithDepthZero() throws IOException {
        final HelloWorldInput input = HelloWorldInput.builder()
                .name(CAPTAIN_HOOK)
                .forward(0)
                .build();

        final Response<HelloWorldOutput> response = getJsonResponse(input, INDEX_URL);
        assertThat(response.getValue().getMessage()).isEqualTo(PORT + " -> Received name: " + CAPTAIN_HOOK);
        assertThat(response.getValue().getRespondingTime()).isNotNull();
        assertThat(response.getMetadata()).containsKeys("timeSpent", "startTime", "endTime");
    }

    @Test
    public void testHelloWorldCallWithDepthZeroWithTwoApplicationsAtSameTime() throws IOException, InterruptedException {
        assertThrows(ConnectException.class, () -> {
            final String s = getUrlContents(ALTERNATIVE_INDEX_URL);
            System.out.println(s);
        });

        try(ConfigurableApplicationContext ignored = startApplication(ALTERNATIVE_PORT)) {
            final String urlContents = getUrlContents(ALTERNATIVE_INDEX_URL);
            assertThat(urlContents).isEqualTo(onlineStatus(ALTERNATIVE_PORT));

            final HelloWorldInput input = HelloWorldInput.builder()
                    .name(CAPTAIN_HOOK)
                    .forward(0)
                    .build();

            final Response<HelloWorldOutput> response = getJsonResponse(input, INDEX_URL);
            assertThat(response.getValue().getMessage()).isEqualTo(PORT + " -> Received name: " + CAPTAIN_HOOK);
            assertThat(response.getValue().getRespondingTime()).isNotNull();
            assertThat(response.getMetadata()).containsKeys("timeSpent", "startTime", "endTime");
        }
    }

    @Test
    public void testHelloWorldCallWithDepthThree() throws IOException, InterruptedException {
        assertThrows(ConnectException.class, () -> {
            final String s = getUrlContents(ALTERNATIVE_INDEX_URL);
            System.out.println(s);
        });

        try(ConfigurableApplicationContext ignored = startApplication(ALTERNATIVE_PORT)) {
            final String urlContents = getUrlContents(ALTERNATIVE_INDEX_URL);
            assertThat(urlContents).isEqualTo(onlineStatus(ALTERNATIVE_PORT));

            final HelloWorldInput input = HelloWorldInput.builder()
                    .name(CAPTAIN_HOOK)
                    .forward(1)
                    .build();

            final Response<HelloWorldOutput> response = getJsonResponse(input, INDEX_URL);
            assertThat(response.getExceptionResult().convertToThrowable().getCause().getMessage()).startsWith("Could not find value for key dev.EU.TestService.server.host");
        }
    }



    private static Response<HelloWorldOutput> getJsonResponse(HelloWorldInput input, final String indexUrl) throws IOException {
        final String payload = URLEncoder.encode(createGson().toJson(input), StandardCharsets.UTF_8.toString());
        final String url = createUrl(payload, indexUrl);
        final String json = getUrlContents(url);
        final TypeToken<Response<HelloWorldOutput>> typeToken = new TypeToken<Response<HelloWorldOutput>>() {};
        return GSON.fromJson(json, typeToken.getType());
    }

    private static Gson createGson() {
        return new GsonBuilder().registerTypeAdapter(Instant.class, (JsonDeserializer<Instant>) (json, type, jsonDeserializationContext)
                -> Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong())).create();
    }

    private static String createUrl(String payload, final String indexUrl) {
        return indexUrl + "/activity?activity=" + ENDPOINT_NAME + "&encoding=" + ENCODING + "&payload=" + payload;
    }

    private static String getUrlContents(String url) throws IOException {
        System.out.println("Calling URL " + url);
        return IOUtils.toString(new URL(url), Charsets.UTF_8);
    }

    private static ConfigurableApplicationContext startApplication(int port) {
        System.out.println("Starting server on port " + port);
        return ServiceMain.startApplication(standardArgs(port));
    }

    private static String[] standardArgs(final int port) {
        String[] args = {
                "--stage=dev",
                "--region=EU",
                "--dev.EU.server.host=" + "localhost",
                "--dev.EU.server.port=" + port
        };
        if(port == ALTERNATIVE_PORT) {
            final String[] dependencyArgs = new String[]{
                    "--*.*.TestService.server.host=localhost",
                    "--*.*.TestService.server.port=8002"
            };
            return ObjectArrays.concat(args, dependencyArgs, String.class);
        }
        return args;
    }

    public static String onlineStatus(final int port) {
        return "The server is online on port " + port + "!";
    }

}
