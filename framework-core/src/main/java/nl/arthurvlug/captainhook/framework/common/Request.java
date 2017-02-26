package nl.arthurvlug.captainhook.framework.common;

import lombok.Value;

@Value
public class Request<I extends Input> {
    private final I input;
}
