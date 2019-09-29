package com.arthurvlug.captainhook.framework.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@AllArgsConstructor
@ToString
public class Response<O extends Output>  {
    private O value;
    private ExceptionResult exceptionResult;
    private Map<String, Object> metadata;

    public static <O extends Output> SuccessResponse<O> success(final O value, final Map<String, Object> metadata) {
        return new SuccessResponse<>(value, metadata);
    }

    public static <O extends Output, T extends Throwable> FailureResponse failure(final T t, final Map<String, Object> metadata) {
        return new FailureResponse<O>(t, metadata);
    }
}
