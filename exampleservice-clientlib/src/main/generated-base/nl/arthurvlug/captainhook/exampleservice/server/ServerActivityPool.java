package nl.arthurvlug.captainhook.[serviceName].server;

import nl.arthurvlug.captainhook.[serviceName].common.[ServiceName]ActivityConfiguration;
import nl.arthurvlug.captainhook.framework.client.AbstractActivityConfiguration;
import nl.arthurvlug.captainhook.framework.server.BaseServerActivityPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class [ServiceName]ServerActivityPool extends BaseServerActivityPool {
    @Autowired
    private [ServiceName]ActivityConfiguration activityConfiguration;

    protected AbstractActivityConfiguration getActivityConfiguration() {
        return activityConfiguration;
    }
}
