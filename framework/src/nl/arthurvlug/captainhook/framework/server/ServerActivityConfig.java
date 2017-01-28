package nl.arthurvlug.captainhook.framework.server;

import com.google.gson.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import nl.arthurvlug.captainhook.framework.common.response.Output;

@Value
@Getter(AccessLevel.PACKAGE)
public class ServerActivityConfig<I extends Input, O extends Output> {
    private AbstractActivity activity;
    private final TypeToken<Request<I>> requestTypeToken;
}
