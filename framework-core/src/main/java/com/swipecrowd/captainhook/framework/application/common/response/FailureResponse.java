package com.swipecrowd.captainhook.framework.application.common.response;

public class FailureResponse<O extends Output> extends Response<O> {
    public FailureResponse(final Throwable e) {
        super(null, new ExceptionResult(e));
    }
}
