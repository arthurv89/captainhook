package nl.arthurvlug.captainhook.framework.common.response;

import lombok.Value;

import java.io.*;

@Value
public class ExceptionResult {
    private final String className;
    private final String message;
    private final byte[] stackTrace;

    public ExceptionResult(final Throwable e) {
        this.className = e.getClass().getName();
        this.message = e.getMessage();
        this.stackTrace = serializeStackTrace(e);
    }

    private byte[] serializeStackTrace(final Throwable throwable) {
        ObjectOutputStream objectOutputStream;
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(throwable.getStackTrace());
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
