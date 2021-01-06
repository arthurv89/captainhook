package com.swipecrowd.captainhook.framework.integration;

import com.google.common.base.Charsets;
import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import com.swipecrowd.captainhook.framework.application.common.response.Response;
import com.swipecrowd.captainhook.test.testservice.ServiceMain;
import com.swipecrowd.captainhook.test.testservice.TestServiceServerProperties;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldInput;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldOutput;
import org.apache.commons.io.IOUtils;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.ObjectArrays.concat;
import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTestUtils {
    public static final Gson GSON = createGson();

    public static final int JAVA_PORT = 8002;
    public static final String JAVA_INDEX_URL = "http://localhost:" + JAVA_PORT;

    public static final int UNUSED_PORT = 8003;
    public static final String UNUSED_PORT_INDEX_URL = "http://localhost:" + UNUSED_PORT;
    public static final String REGION = "EU";
    public static final String STAGE = "dev";
    public static final String TEST_SERVICE = "TestService";
    public static final String HOSTNAME = "localhost";
    public static final String NO_PAYLOAD = "{}";


    private static final String ENDPOINT_NAME = "HelloWorld";
    private static final String ENCODING = "JSON";

    static final String CAPTAIN_HOOK = "Captain Hook";

    public static Response<HelloWorldOutput> getJsonResponse(final HelloWorldInput input,
                                                             final String indexUrl) throws IOException, InterruptedException {
        final String inputJson = createGson().toJson(input);
        final String url = createUrl(indexUrl);
        final HttpResponse<String> response = getResponse(url, inputJson);
        if(response.statusCode() != 200) {
            try {
                final String message = (String) createGson().fromJson(response.body(), Map.class).get("message");
                throw new RuntimeException(message);
            } catch (RuntimeException e) {
                return Response.failure(e);
            }
        }
        final String outputJson = response.body();
        final TypeToken<HelloWorldOutput> typeToken = new TypeToken<HelloWorldOutput>() {};
        final HelloWorldOutput output = GSON.fromJson(outputJson, typeToken.getType());
        return Response.success(output);
    }

    public static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Instant.class, (JsonDeserializer<Instant>) (json, type, jsonDeserializationContext)
                        -> Instant.parse(json.getAsString())).create();
    }

    public static String createUrl(final String indexUrl) {
        return String.format("%s/%s", indexUrl, ENDPOINT_NAME.toLowerCase());
    }

    public static HttpResponse<String> getResponse(String sUrl, String payload) throws IOException, InterruptedException {
        System.out.printf("Calling URL %s with payload %s%n", sUrl, payload);

        final HttpClient client = HttpClient.newHttpClient();
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(sUrl))
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        return client.send(request, BodyHandlers.ofString());
    }

    public static ConfigurableApplicationContext startApplication(int port, final Optional<Integer> dependencyPort) {
        System.out.println("Starting server on port " + port);
        return ServiceMain.startApplication(standardArgs(port, dependencyPort));
    }

    public static String[] standardArgs(final int port, final Optional<Integer> dependencyPort, String... extraArgs) {
        String[] args = {
                "--stage=" + STAGE,
                "--region=" + REGION,
                "--*.*.server.port=" + port,
                "--*.*.javaStartApplicationArgument=javaStartApplicationArgumentValue"
        };
        if(dependencyPort.isPresent()) {
            final String[] dependencyArgs = new String[]{
                    "--*.*.TestService.server.host=" + HOSTNAME,
                    "--*.*.TestService.server.port=" + dependencyPort.get()
            };
            return concat(args, concat(dependencyArgs, extraArgs, String.class), String.class);
        }
        return concat(args, extraArgs, String.class);
    }

    public static void verifyProperties(final int port, final TestServiceServerProperties testServiceServerProperties) throws IOException {
        assertThat(testServiceServerProperties.getStage()).isEqualTo(STAGE);
        assertThat(testServiceServerProperties.getRegion()).isEqualTo(REGION);
        assertThat(testServiceServerProperties.getApplicationArguments().get("stage")).hasValue(STAGE);
        assertThat(testServiceServerProperties.getApplicationArguments().get("region")).hasValue(REGION);
        assertThat(testServiceServerProperties.getApplicationArguments().get("*.*.server.port")).hasValue(String.valueOf(port));
        assertThat(testServiceServerProperties.getApplicationArguments().get("*.*.name")).hasValue(TEST_SERVICE);
        assertThat(testServiceServerProperties.getPort()).isEqualTo(port);
        assertThat(testServiceServerProperties.getName()).isEqualTo(TEST_SERVICE);
    }

    public static TestServiceServerProperties getTestServiceServerProperties(final String indexUrl) throws IOException, InterruptedException {
        String message = getConfigContents(indexUrl);
        return createGson().fromJson(message, TestServiceServerProperties.class);
    }

    public static String getConfigContents(final String indexUrl) throws IOException {
        return getContents(indexUrl + "/config");
    }

    public static String getContents(final String url) throws IOException {
        try {
            return IOUtils.toString(new URL(url), Charsets.UTF_8);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
