package nl.arthurvlug.captainhook.framework.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.arthurvlug.captainhook.framework.common.response.Output;
import nl.arthurvlug.captainhook.framework.server.IOType;
import nl.arthurvlug.captainhook.framework.server.Input;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseClientActivityPool extends AbstractClientActivityPool {
    private Map<String, ClientActivityConfig<?,?>> map;

    @PostConstruct
    private void init() {
        map = getActivityConfiguration().getMap().entrySet().stream()
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
        final IOType<? extends Input, ? extends Output> ioType = getActivityConfiguration().getIOType(key);
        return new ClientActivityConfig<>(ioType.getRequestTypeToken(), ioType.getResponseTypeToken());
    }

    protected abstract AbstractActivityConfiguration getActivityConfiguration();
}
