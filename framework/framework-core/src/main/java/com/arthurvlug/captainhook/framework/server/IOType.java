package com.arthurvlug.captainhook.framework.server;

import com.arthurvlug.captainhook.framework.common.response.Output;
import com.arthurvlug.captainhook.framework.common.response.Response;
import com.google.gson.reflect.TypeToken;
import lombok.Value;

@Value
public class IOType<I extends Input, O extends Output> {
    private final TypeToken<Request<I>> requestType;
    private final TypeToken<Response<O>> responseType;
}
