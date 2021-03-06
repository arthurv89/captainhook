package com.swipecrowd.captainhook.framework.application.common.response;

import lombok.Getter;

import java.util.Optional;

public class DependencyException extends RuntimeException {
    @Getter
    private final Optional<String> clientName;

    public DependencyException(Exception e, final String clientName) {
        super(e);
        this.clientName = Optional.of(clientName);
    }

    public DependencyException(String message) {
        super(message);
        this.clientName = Optional.empty();
    }
}
