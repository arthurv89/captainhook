package nl.arthurvlug.captainhook.framework.common.response;

public class Response<T> {
    private T value;
    private ExceptionResult exceptionResult;

    protected Response(final T value, final ExceptionResult exceptionResult) {
        this.value = value;
        this.exceptionResult = exceptionResult;
    }

    public static <T, E extends ExceptionResult> SuccessResponse success(final T value) {
        return new SuccessResponse<T>(value);
    }

    public static <T, E extends Exception> FailureResponse failure(final E e) {
        return new FailureResponse<T>(e);
    }


    public T getValue() {
        return value;
    }

    public ExceptionResult getExceptionResult() {
        return exceptionResult;
    }

}
