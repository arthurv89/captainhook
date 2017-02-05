package nl.arthurvlug.captainhook.exampleservice2.activity.helloworld;

import lombok.Builder;
import lombok.Getter;
import nl.arthurvlug.captainhook.framework.server.Input;

@Builder
@Getter
public class HelloWorldInput extends Input {
    private final String name;
}
