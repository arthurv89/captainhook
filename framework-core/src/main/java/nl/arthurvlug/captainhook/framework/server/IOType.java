package nl.arthurvlug.captainhook.framework.server;

import com.google.gson.reflect.TypeToken;
import lombok.Value;
import nl.arthurvlug.captainhook.framework.common.response.Output;
import nl.arthurvlug.captainhook.framework.common.response.Response;

@Value
public class IOType<I extends Input, O extends Output> {
    private final TypeToken<Request<I>> requestTypeToken;
    private final TypeToken<Response<O>> responseTypeToken;
}
