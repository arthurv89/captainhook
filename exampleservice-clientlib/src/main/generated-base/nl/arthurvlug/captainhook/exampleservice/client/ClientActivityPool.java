package nl.arthurvlug.captainhook.[serviceName].client;

import lombok.Getter;
import nl.arthurvlug.captainhook.[serviceName].common.[ServiceName]ActivityConfiguration;
import nl.arthurvlug.captainhook.framework.client.BaseClientActivityPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class [ServiceName]ClientActivityPool extends BaseClientActivityPool {
    @Autowired
    private [ServiceName]ActivityConfiguration activityConfiguration;
}

