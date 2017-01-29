package nl.arthurvlug.captainhook.exampleservice.server.activity;

import nl.arthurvlug.captainhook.framework.server.AbstractActivity;
import nl.arthurvlug.captainhook.framework.server.Input;
import nl.arthurvlug.captainhook.framework.common.response.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractExampleActivity<I extends Input, O extends Output>
        extends AbstractActivity<I, O, ExampleServiceRequestContext> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected ExampleServiceRequestContext preActivity(final I input) {
        return new ExampleServiceRequestContext(System.nanoTime());
    }

    @Override
    protected void postActivity(final O output, final ExampleServiceRequestContext requestContext) {
        final long endTime = System.nanoTime();
        final long spentTime = endTime - requestContext.getStartingTime();
        logger.info("[" + requestContext.getRequestId() + "] Activity took " + (spentTime / 1000000) + " ms");
    }
}
