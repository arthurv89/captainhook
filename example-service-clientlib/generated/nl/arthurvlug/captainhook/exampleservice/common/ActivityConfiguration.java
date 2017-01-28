package nl.arthurvlug.captainhook.exampleservice.common;

import com.google.common.collect.ImmutableMap;
import com.google.gson.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldInput;
import nl.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldOutput;
import nl.arthurvlug.captainhook.framework.server.IOType;
import nl.arthurvlug.captainhook.framework.server.Input;
import nl.arthurvlug.captainhook.framework.server.Request;
import nl.arthurvlug.captainhook.framework.common.response.Output;
import nl.arthurvlug.captainhook.framework.common.response.Response;
import nl.arthurvlug.captainhook.framework.client.AbstractActivityConfiguration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivityConfiguration extends AbstractActivityConfiguration {
    public static final String HELLO_WORLD = "HelloWorld";

    protected Map<String, IOType<?, ?>> getMap() {
        return ImmutableMap.<String, IOType<? extends Input, ? extends Output>> builder()
                .put(HELLO_WORLD, new IOType<>(new TypeToken<Request<HelloWorldInput>>() {}, new TypeToken<Response<HelloWorldOutput>>() {}))
                .build();
    }
}
