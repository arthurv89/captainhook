package nl.arthurvlug.captainhook.framework.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.arthurvlug.captainhook.framework.server.IOType;
import nl.arthurvlug.captainhook.framework.server.Input;
import nl.arthurvlug.captainhook.framework.common.response.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientActivityPool extends AbstractClientActivityPool {
    @Autowired
    private AbstractActivityConfiguration activityConfiguration;

    private Map<String, ClientActivityConfig<?,?>> map;

    @PostConstruct
    private void init() {
        map = activityConfiguration.getMap().entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> activityConfig(e.getKey())
                ));
    }

    @Override
    protected ClientActivityConfig<?,?> get(final String activity) {
        return map.get(activity);
    }

    private ClientActivityConfig<? extends Input, ? extends Output> activityConfig(final String key) {
        final IOType<? extends Input, ? extends Output> ioType = activityConfiguration.getIOType(key);
        return new ClientActivityConfig<>(ioType.getRequestTypeToken(), ioType.getResponseTypeToken());
    }
}
