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
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTestUtils {
    private static final Gson GSON = createGson();

    public static final int JAVA_PORT = 8002;
    public static final String JAVA_INDEX_URL = "http://localhost:" + JAVA_PORT;

    public static final int UNUSED_PORT = 8003;
    public static final String UNUSED_PORT_INDEX_URL = "http://localhost:" + UNUSED_PORT;
    public static final String REGION = "EU";
    public static final String STAGE = "dev";
    public static final String TEST_SERVICE = "TestService";
    public static final String HOSTNAME = "localhost";


    private static final String ENDPOINT_NAME = "HelloWorld";
    private static final String ENCODING = "JSON";

    static final String CAPTAIN_HOOK = "Captain Hook";

    public static Response<HelloWorldOutput> getJsonResponse(HelloWorldInput input, final String indexUrl) throws IOException {
        final String payload = URLEncoder.encode(createGson().toJson(input), StandardCharsets.UTF_8.toString());
        final String url = createUrl(payload, indexUrl);
        final String json = getUrlContents(url);
        final TypeToken<Response<HelloWorldOutput>> typeToken = new TypeToken<Response<HelloWorldOutput>>() {};
        return GSON.fromJson(json, typeToken.getType());
    }

    public static Gson createGson() {
        return new GsonBuilder().registerTypeAdapter(Instant.class, (JsonDeserializer<Instant>) (json, type, jsonDeserializationContext)
                -> Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong())).create();
    }

    public static String createUrl(String payload, final String indexUrl) {
        return indexUrl + "/activity?activity=" + ENDPOINT_NAME + "&encoding=" + ENCODING + "&payload=" + payload;
    }

    public static String getUrlContents(String url) throws IOException {
        System.out.println("Calling URL " + url);
        return IOUtils.toString(new URL(url), Charsets.UTF_8);
    }

    public static ConfigurableApplicationContext startApplication(int port, final Optional<Integer> dependencyPort) {
        System.out.println("Starting server on port " + port);
        return ServiceMain.startApplication(standardArgs(port, dependencyPort));
    }

    public static String[] standardArgs(final int port, final Optional<Integer> dependencyPort) {
        String[] args = {
                "--stage=" + STAGE,
                "--region=" + REGION,
                "--*.*.server.port=" + port
        };
        if(dependencyPort.isPresent()) {
            final String[] dependencyArgs = new String[]{
                    "--*.*.TestService.server.host=" + HOSTNAME,
                    "--*.*.TestService.server.port=" + dependencyPort.get()
            };
            return ObjectArrays.concat(args, dependencyArgs, String.class);
        }
        return args;
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

    public static String onlineStatus(final int port) {
        return "The server is online on port " + port + "!";
    }

}
