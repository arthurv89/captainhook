package nl.arthurvlug.captainhook.framework.server;

import lombok.Getter;

import java.util.UUID;

@Getter
public class DefaultRequestContext extends AbstractRequestContext {
    private final String requestId = UUID.randomUUID().toString();
}
