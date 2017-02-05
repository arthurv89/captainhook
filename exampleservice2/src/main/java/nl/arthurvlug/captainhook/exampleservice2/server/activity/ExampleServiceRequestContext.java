package nl.arthurvlug.captainhook.exampleservice2.server.activity;

import nl.arthurvlug.captainhook.framework.server.DefaultRequestContext;

public class ExampleServiceRequestContext extends DefaultRequestContext {
    final Long startingTime;

    public ExampleServiceRequestContext(final Long startingTime) {
        this.startingTime = startingTime;
    }

    long getStartingTime() {
        return startingTime;
    }
}
