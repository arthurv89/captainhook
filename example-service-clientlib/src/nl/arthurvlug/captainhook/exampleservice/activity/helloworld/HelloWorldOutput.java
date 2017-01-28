package nl.arthurvlug.captainhook.exampleservice.activity.helloworld;

import lombok.Value;
import nl.arthurvlug.captainhook.framework.common.response.Output;

@Value
public class HelloWorldOutput extends Output {
    private final String message;
}
