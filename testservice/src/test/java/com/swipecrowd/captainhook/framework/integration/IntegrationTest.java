package com.swipecrowd.captainhook.framework.integration;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import com.swipecrowd.captainhook.framework.common.response.Response;
import com.swipecrowd.captainhook.test.testservice.ServiceMain;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldInput;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldOutput;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IntegrationTest {
    public static final int PORT = 8001;
    public static final String INDEX_URL = "http://localhost:" + PORT;

    public static final int ALTERNATIVE_PORT = 8002;
    public static final String ALTERNATIVE_INDEX_URL = "http://localhost:" + ALTERNATIVE_PORT;

    public static final int UNUSED_PORT = 8003;
    public static final String UNUSED_PORT_INDEX_URL = "http://localhost:" + UNUSED_PORT;

    public static final String ONLINE_STATUS = "The server is online!";

    private static final Gson GSON = createGson();
    private static final String ENDPOINT_NAME = "HelloWorld";
    private static final String ENCODING = "JSON";

    private static final String CAPTAIN_HOOK = "Captain Hook";
    private static ConfigurableApplicationContext standardApplicationContext;

    @BeforeAll
    public static void beforeAll() {
        standardApplicationContext = startApplication(PORT);
    }

    @AfterAll
    public static void afterAll() {
        standardApplicationContext.close();
    }

    @Test
    public void testStatus() throws IOException {
        final String urlContents = getUrlContents(INDEX_URL);
        assertThat(urlContents).isEqualTo(ONLINE_STATUS);
    }

    @Test
    public void testNoStatus() {
        assertThrows(ConnectException.class, () -> {
            getUrlContents(UNUSED_PORT_INDEX_URL);
        });
    }

    @Test
    public void testHelloWorldCallWithDepthZero() throws IOException {
        final HelloWorldInput input = HelloWorldInput.builder()
                .name(CAPTAIN_HOOK)
                .forward(0)
                .build();
        final String payload = URLEncoder.encode(createGson().toJson(input), StandardCharsets.UTF_8.toString());

        final Response<HelloWorldOutput> response = getJsonResponse(createUrl(payload, INDEX_URL));
        assertThat(response.getValue().getMessage()).isEqualTo("Received name: " + CAPTAIN_HOOK);
        assertThat(response.getValue().getRespondingTime()).isNotNull();
        assertThat(response.getMetadata()).containsKeys("timeSpent", "startTime", "endTime");
    }

    @Test
    public void testHelloWorldCallWithDepthThree() throws IOException {
        assertThrows(ConnectException.class, () -> {
            final String s = getUrlContents(ALTERNATIVE_INDEX_URL);
            System.out.println(s);
        });

        try(ConfigurableApplicationContext ignored = startApplication(ALTERNATIVE_PORT)) {
            final String urlContents = getUrlContents(ALTERNATIVE_INDEX_URL);
            assertThat(urlContents).isEqualTo(ONLINE_STATUS);

            final HelloWorldInput input = HelloWorldInput.builder()
                    .name(CAPTAIN_HOOK)
                    .forward(1)
                    .build();
            final String payload = URLEncoder.encode(createGson().toJson(input), StandardCharsets.UTF_8.toString());

            final Response<HelloWorldOutput> response = getJsonResponse(createUrl(payload, ALTERNATIVE_INDEX_URL));
            assertThat(response.getValue().getMessage()).isEqualTo("Received name: " + CAPTAIN_HOOK);
            assertThat(response.getValue().getRespondingTime()).isNotNull();
            assertThat(response.getMetadata()).containsKeys("timeSpent", "startTime", "endTime");
        }
    }

    private String createUrl(String payload, final String url) {
        return url + "/activity?activity=" + ENDPOINT_NAME + "&encoding=" + ENCODING + "&payload=" + payload;
    }

    private static Gson createGson() {
        return new GsonBuilder().registerTypeAdapter(Instant.class, (JsonDeserializer<Instant>) (json, type, jsonDeserializationContext)
                -> Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong())).create();
    }

    private Response<HelloWorldOutput> getJsonResponse(String s) throws IOException {
        final String json = getUrlContents(s);
        final TypeToken<Response<HelloWorldOutput>> typeToken = new TypeToken<Response<HelloWorldOutput>>() {};
        return GSON.fromJson(json, typeToken.getType());
    }

    private String getUrlContents(String s) throws IOException {
        return IOUtils.toString(new URL(s), Charsets.UTF_8);
    }

    private static ConfigurableApplicationContext startApplication(int port) {
        System.out.println("Starting server on port " + port);
        return ServiceMain.startApplication(standardArgs(port));
    }

    private static String[] standardArgs(final int port) {
        return new String[]{
                "--stage=dev",
                "--region=EU",
                "--dev.EU.server.host=" + "localhost",
                "--dev.EU.server.port=" + port
        };
    }

}
