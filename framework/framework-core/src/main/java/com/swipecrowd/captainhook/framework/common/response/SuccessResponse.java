package com.swipecrowd.captainhook.framework.common.response;

import java.util.Map;

public class SuccessResponse<O extends Output> extends Response<O> {
    public SuccessResponse(final O value, final Map<String, Object> metadata) {
        super(value, null, metadata);
    }
}
