package nl.arthurvlug.captainhook.exampleservice.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldOutput;
import nl.arthurvlug.captainhook.framework.client.AbstractService;
import nl.arthurvlug.captainhook.framework.server.Call;
import nl.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldInput;
import org.springframework.stereotype.Component;

import static nl.arthurvlug.captainhook.exampleservice.common.ActivityConfiguration.*;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExampleServiceClient extends AbstractService {
    public Call<HelloWorldOutput> helloWorldCall(final HelloWorldInput helloWorldInput) {
        return createCall(HELLO_WORLD, helloWorldInput);
    }
}
