package nl.arthurvlug.captainhook.exampleservice.activity.helloworld;

import lombok.EqualsAndHashCode;
import lombok.Value;
import nl.arthurvlug.captainhook.framework.common.response.Output;

@Value
@EqualsAndHashCode(callSuper = false)
public class HelloWorldOutput extends Output {
    private final String message;
}
