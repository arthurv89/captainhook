package nl.arthurvlug.captainhook.framework.common.response;

import com.google.common.base.Throwables;
import lombok.Value;

import java.io.*;

@Value
public class ExceptionResult {
    private final byte[] bytes;

    public ExceptionResult(final Throwable throwable) {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(throwable);
            }
            bytes = b.toByteArray();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public Throwable convertToThrowable() {
        try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                return (Throwable) o.readObject();
            } catch (ClassNotFoundException e) {
                throw Throwables.propagate(e);
            }
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}
