package nl.arthurvlug.captainhook.exampleservice.activity.helloworld;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import nl.arthurvlug.captainhook.framework.common.response.Output;

import java.util.Calendar;

@Builder
@Value
@EqualsAndHashCode(callSuper = false)
public class HelloWorldOutput extends Output {
    private String message;
    private Calendar respondingTime;
}
