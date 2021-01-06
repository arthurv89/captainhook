package com.swipecrowd.captainhook.framework.application.common.response;

public class SuccessResponse<O extends Output> extends Response<O> {
    public SuccessResponse(final O value) {
        super(value, null);
    }
}
