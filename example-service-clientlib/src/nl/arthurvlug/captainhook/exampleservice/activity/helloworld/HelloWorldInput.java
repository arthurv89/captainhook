package nl.arthurvlug.captainhook.exampleservice.activity.helloworld;

import nl.arthurvlug.captainhook.framework.server.Input;

public class HelloWorldInput extends Input {
    private final String name;

    public HelloWorldInput(final String name) {
        this.name = name;
    }

    public static HelloWorldInput.Builder builder() {
        return new Builder();
    }

    String getName() {
        return name;
    }

    public static class Builder {
        private String name;

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public HelloWorldInput build() {
            return new HelloWorldInput(name);
        }
    }
}
