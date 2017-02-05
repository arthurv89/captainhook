package nl.arthurvlug.captainhook.[serviceName].common;

import com.google.common.collect.ImmutableMap;
import com.google.gson.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.arthurvlug.captainhook.[serviceName].activity.[endpoint].[Endpoint]Input;
import nl.arthurvlug.captainhook.[serviceName].activity.[endpoint].[Endpoint]Output;
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
public class [ServiceName]ActivityConfiguration extends AbstractActivityConfiguration {
    public static final String [Endpoint]Endpoint = "[Endpoint]";

    protected Map<String, IOType<?, ?>> getMap() {
        return ImmutableMap.<String, IOType<? extends Input, ? extends Output>> builder()
                .put([Endpoint]Endpoint, new IOType<>(new TypeToken<Request<[Endpoint]Input>>() {}, new TypeToken<Response<[Endpoint]Output>>() {}))
        .build();
    }
}
