package nl.arthurvlug.captainhook.exampleservice.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.arthurvlug.captainhook.exampleservice.activity.xxx.XXXOutput;
import nl.arthurvlug.captainhook.framework.client.AbstractService;
import nl.arthurvlug.captainhook.framework.server.Call;
import nl.arthurvlug.captainhook.exampleservice.activity.xxx.XXXInput;
import org.springframework.stereotype.Component;

import static nl.arthurvlug.captainhook.exampleservice.common.ActivityConfiguration.*;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExampleServiceClient extends AbstractService {
    public Call<XXXOutput> xXXCall(final XXXInput input) {
        return createCall(XXXEndpoint, input);
    }
}
