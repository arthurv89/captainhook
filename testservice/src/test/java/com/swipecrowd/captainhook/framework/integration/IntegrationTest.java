package com.swipecrowd.captainhook.framework.integration;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import com.swipecrowd.captainhook.framework.common.response.Response;
import com.swipecrowd.captainhook.tutorial.testservice.ServiceMain;
import com.swipecrowd.captainhook.tutorial.testservice.activity.helloworld.HelloWorldOutput;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest {
    @BeforeAll
    public static void beforeAll() {
        String[] args = new String[0];
        ServiceMain.main(args);
    }

    @Test
    public void testStatus() throws InterruptedException, IOException {
        String urlContents = getUrlContents("http://localhost:1234");
        assertThat(urlContents).isEqualTo("The server is online!");
    }

    @Test
    public void testHelloWorldCall() throws InterruptedException, IOException {
        String payload = URLEncoder.encode("{\"name\":\"Captain Hook\"}", StandardCharsets.UTF_8.toString());
        String functionName = "HelloWorld";

        Response<HelloWorldOutput> response = getJsonResponse(url(payload, functionName));
        assertThat(response.getValue().getMessage()).isEqualTo("HelloMoonService received HelloWorldInput with name property: \"Captain Hook\"");
        assertThat(response.getValue().getRespondingTime()).isNotNull();
        assertThat(response.getMetadata()).containsKeys("timeSpent", "startTime", "endTime");
    }

    private String url(String payload, String functionName) {
        String encoding = "JSON";
        return "http://localhost:1234/activity?activity=" + functionName + "&encoding=" + encoding + "&payload=" + payload;
    }

    private Gson createGson() {
        return new GsonBuilder().registerTypeAdapter(Instant.class, (JsonDeserializer<Instant>) (json, type, jsonDeserializationContext)
                -> Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong())).create();
    }

    private Response<HelloWorldOutput> getJsonResponse(String s) throws IOException {
        String json = getUrlContents(s);
        TypeToken<Response<HelloWorldOutput>> typeToken = new TypeToken<Response<HelloWorldOutput>>() {};
        return createGson().fromJson(json, typeToken.getType());
    }

    private String getUrlContents(String s) throws IOException {
        return IOUtils.toString(new URL(s), Charsets.UTF_8);
    }
}
