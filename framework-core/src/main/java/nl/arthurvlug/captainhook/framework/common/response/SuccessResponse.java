package nl.arthurvlug.captainhook.framework.common.response;

public class SuccessResponse<T> extends Response<T> {
    public SuccessResponse(final T value) {
        super(value, null);
    }
}
