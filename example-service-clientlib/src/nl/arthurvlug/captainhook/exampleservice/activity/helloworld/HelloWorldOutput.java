package nl.arthurvlug.captainhook.exampleservice.activity.helloworld;

import nl.arthurvlug.captainhook.framework.common.response.Output;

public class HelloWorldOutput extends Output {
    private final String name;

    public HelloWorldOutput(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
