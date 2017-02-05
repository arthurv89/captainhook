package nl.arthurvlug.captainhook.exampleservice2.activity.ping;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import nl.arthurvlug.captainhook.framework.common.response.Output;

@Builder
@Value
@EqualsAndHashCode(callSuper = false)
public class PingOutput extends Output {
    private String y;
}
