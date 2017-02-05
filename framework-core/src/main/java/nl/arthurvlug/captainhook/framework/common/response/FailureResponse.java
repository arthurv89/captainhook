package nl.arthurvlug.captainhook.framework.common.response;

public class FailureResponse<T> extends Response<T> {
    public FailureResponse(final Throwable e) {
        super(null, new ExceptionResult(e));
    }
}
