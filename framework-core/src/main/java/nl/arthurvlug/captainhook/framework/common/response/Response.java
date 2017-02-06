package nl.arthurvlug.captainhook.framework.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@AllArgsConstructor
@ToString
public class Response<O extends Output>  {
    private O value;
    private ExceptionResult exceptionResult;
    private Map<String, Object> metadata;

    public static <T extends Output> SuccessResponse<T> success(final T value, final Map<String, Object> metadata) {
        return new SuccessResponse<>(value, metadata);
    }

    public static <T extends Output, E extends Throwable> FailureResponse failure(final E e, final Map<String, Object> metadata) {
        return new FailureResponse<T>(e, metadata);
    }
}
