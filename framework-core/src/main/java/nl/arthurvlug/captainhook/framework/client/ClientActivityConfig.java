package nl.arthurvlug.captainhook.framework.client;

import com.google.gson.reflect.TypeToken;
import lombok.Value;
import nl.arthurvlug.captainhook.framework.server.Input;
import nl.arthurvlug.captainhook.framework.server.Request;
import nl.arthurvlug.captainhook.framework.common.response.Response;
import nl.arthurvlug.captainhook.framework.common.response.Output;

@Value
public class ClientActivityConfig<I extends Input, O extends Output>  {
    private final TypeToken<Request<I>> requestTypeToken;
    private TypeToken<Response<O>> responseTypeToken;
}
