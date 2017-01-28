package nl.arthurvlug.captainhook.framework.server;

import lombok.Value;

@Value
public class Request<I> {
    private final I input;
}
