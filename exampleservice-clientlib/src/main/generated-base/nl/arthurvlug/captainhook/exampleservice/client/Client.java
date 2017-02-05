package nl.arthurvlug.captainhook.[serviceName].client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.arthurvlug.captainhook.[serviceName].activity.[endpoint].[Endpoint]Output;
import nl.arthurvlug.captainhook.framework.client.AbstractService;
import nl.arthurvlug.captainhook.framework.server.Call;
import nl.arthurvlug.captainhook.[serviceName].activity.[endpoint].[Endpoint]Input;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import static nl.arthurvlug.captainhook.[serviceName].common.[ServiceName]ActivityConfiguration.*;

@Component
public class [ServiceName]Client extends AbstractService {
    @Autowired
    private [ServiceName]Client(final [ServiceName]ClientActivityPool clientActivityPool,
                                final [ServiceName]ClientConfigurationComponent clientConfigurationComponent) {
        super(clientActivityPool, clientConfigurationComponent);
    }

    public Call<[Endpoint]Output> [Endpoint]Call(final [Endpoint]Input input) {
        return createCall([Endpoint]Endpoint, input);
    }
}
