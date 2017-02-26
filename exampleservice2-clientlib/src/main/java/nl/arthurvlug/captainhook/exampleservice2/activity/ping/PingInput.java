package nl.arthurvlug.captainhook.exampleservice2.activity.ping;

import lombok.Builder;
import lombok.Getter;
import nl.arthurvlug.captainhook.framework.common.Input;

@Builder
@Getter
public class PingInput extends Input {
    private final String x;
}
