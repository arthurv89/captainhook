package nl.arthurvlug.captainhook.framework.common.response;

import lombok.Getter;

public class DependencyException extends RuntimeException {
    @Getter
    private final String clientName;

    public DependencyException(Exception e, final String clientName) {
        super(e);
        this.clientName = clientName;
    }
}
