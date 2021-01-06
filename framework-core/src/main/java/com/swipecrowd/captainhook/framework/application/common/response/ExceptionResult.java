package com.swipecrowd.captainhook.framework.application.common.response;

import lombok.Value;
import org.springframework.util.SerializationUtils;

@Value
public class ExceptionResult {
    private final byte[] bytes;

    public ExceptionResult(final Throwable throwable) {
        bytes = SerializationUtils.serialize(throwable);
    }

    public Throwable convertToThrowable() {
        return (Throwable) SerializationUtils.deserialize(bytes);
    }
}
