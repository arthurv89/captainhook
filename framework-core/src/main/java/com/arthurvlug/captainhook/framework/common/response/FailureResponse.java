package com.arthurvlug.captainhook.framework.common.response;

import java.util.Map;

public class FailureResponse<O extends Output> extends Response<O> {
    public FailureResponse(final Throwable e, final Map<String, Object> metadata) {
        super(null, new ExceptionResult(e), metadata);
    }
}
