package nl.arthurvlug.captainhook.exampleservice.server.activity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import nl.arthurvlug.captainhook.framework.server.DefaultRequestContext;

@EqualsAndHashCode(callSuper = true)
@Value
@AllArgsConstructor
public class ExampleServiceRequestContext extends DefaultRequestContext {
    final Long startingTime;
}
