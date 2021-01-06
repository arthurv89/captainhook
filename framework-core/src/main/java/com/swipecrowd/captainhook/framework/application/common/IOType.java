package com.swipecrowd.captainhook.framework.application.common;

import com.swipecrowd.captainhook.framework.application.common.response.Output;
import com.swipecrowd.captainhook.framework.application.common.response.Response;
import com.google.gson.reflect.TypeToken;
import lombok.Value;

@Value
public class IOType<I extends Input, O extends Output> {
    TypeToken<Request<I>> requestType;
    TypeToken<Response<O>> responseType;
}
