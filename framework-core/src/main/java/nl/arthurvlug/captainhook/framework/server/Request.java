package nl.arthurvlug.captainhook.framework.server;

import lombok.Value;

@Value
public class Request<I extends Input> {
    private final I input;
}
