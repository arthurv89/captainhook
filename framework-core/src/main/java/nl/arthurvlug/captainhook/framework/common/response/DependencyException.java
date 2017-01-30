package nl.arthurvlug.captainhook.framework.common.response;

public class DependencyException extends RuntimeException {
    public DependencyException(Exception e) {
        super(e);
    }
}
