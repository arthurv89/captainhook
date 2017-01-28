package nl.arthurvlug.captainhook.framework.common.response;

public class FailureResponse<T> extends Response<T> {
    public FailureResponse(final Exception e) {
        super(null, new ExceptionResult(e));
    }
}
