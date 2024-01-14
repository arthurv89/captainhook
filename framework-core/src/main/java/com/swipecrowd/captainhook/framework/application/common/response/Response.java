package com.swipecrowd.captainhook.framework.application.common.response;

import com.swipecrowd.captainhook.framework.application.common.Output;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Response<O extends Output>  {
    private O value;
    private ExceptionResult exceptionResult;

    public static <O extends Output> SuccessResponse<O> success(final O value) {
        return new SuccessResponse<>(value);
    }

    public static <O extends Output, T extends Throwable> FailureResponse failure(final T t) {
        return new FailureResponse<O>(t);
    }
}
