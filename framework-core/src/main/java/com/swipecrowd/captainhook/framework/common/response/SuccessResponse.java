package com.swipecrowd.captainhook.framework.common.response;

public class SuccessResponse<O extends Output> extends Response<O> {
    public SuccessResponse(final O value) {
        super(value, null);
    }
}
