package nl.arthurvlug.captainhook.framework.server;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.arthurvlug.captainhook.framework.client.AbstractActivityConfiguration;
import nl.arthurvlug.captainhook.framework.common.response.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerActivityPool extends AbstractServerActivityPool {
    private Map<String, ServerActivityConfig<? extends Input, ? extends Output>> map;

    @Autowired
    private ActivityScanner activityScanner;

    @Autowired
    private AbstractActivityConfiguration activityConfiguration;

    @PostConstruct
    private void init() {
        map = activityScanner.scan().entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey(),
                e -> activityConfig(e.getKey(), e.getValue())));
    }

    private ServerActivityConfig<? extends Input, ? extends Output> activityConfig(final String key, final AbstractActivity value) {
        final IOType<? extends Input, ? extends Output> ioType = activityConfiguration.getIOType(key);
        return new ServerActivityConfig<>(value, ioType.getRequestTypeToken());
    }

    protected ServerActivityConfig<? extends Input, ? extends Output> get(final String activity) {
        return map.get(activity);
    }
}
