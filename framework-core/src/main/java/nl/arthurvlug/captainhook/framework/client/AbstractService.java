package nl.arthurvlug.captainhook.framework.client;

import com.google.gson.reflect.TypeToken;
import nl.arthurvlug.captainhook.framework.common.response.DependencyException;
import nl.arthurvlug.captainhook.framework.common.response.ExceptionResult;
import nl.arthurvlug.captainhook.framework.common.response.Output;
import nl.arthurvlug.captainhook.framework.common.response.Response;
import nl.arthurvlug.captainhook.framework.common.serialization.Serializer;
import nl.arthurvlug.captainhook.framework.common.serialization.SerializerTypes;
import nl.arthurvlug.captainhook.framework.server.Call;
import nl.arthurvlug.captainhook.framework.server.Input;
import nl.arthurvlug.captainhook.framework.server.Request;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractService {
    private static final Serializer SERIALIZER = SerializerTypes.JSON.getSerializer();

    private AbstractClientActivityPool clientActivityPool;

    private AbstractClientConfiguration clientConfiguration;

    protected AbstractService(final AbstractClientActivityPool clientActivityPool,
                              final AbstractClientConfiguration clientConfiguration) {
        this.clientActivityPool = clientActivityPool;
        this.clientConfiguration = clientConfiguration;
    }

    protected <I extends Input, O extends Output> Call<O> createCall(final String activity,
                                                                     final I input) {
        return () -> get(activity, input);
    }

    private <I extends Input, O extends Output> O get(final String activity,
                                                      final I input) {
        final TypeToken<Response<O>> outputTypeToken = getResponseTypeToken(activity);

        final byte[] bytes = getByteResponse(activity, input);

        final Response<O> response = SERIALIZER.deserialize(bytes, outputTypeToken);
        if(response.getExceptionResult() == null) {
            return response.getValue();
        } else {
            final ExceptionResult exceptionResult = response.getExceptionResult();
            final Exception exception = createException(exceptionResult);
            throw new DependencyException(exception);
        }
    }

    private <I extends Input, O extends Output> TypeToken<Response<O>> getResponseTypeToken(final String activity) {
        final ClientActivityConfig<I, O> inputOutputClientActivityConfig = clientActivityPool.get(activity);
        return inputOutputClientActivityConfig.getResponseTypeToken();
    }

    private <I extends Input> byte[] getByteResponse(final String activity,
                                                     final I input) {
        try {
            return readBytes(activity, input);
        } catch (IOException e) {
            throw new DependencyException(e);
        }
    }

    private Exception createException(final ExceptionResult exceptionResult)  {
        final Exception exception;
        try {
            exception = getExceptionClass(exceptionResult)
                    .getConstructor(String.class)
                    .newInstance(exceptionResult.getMessage());
            exception.setStackTrace(getStackTrace(exceptionResult));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return exception;
    }

    public <T extends Exception> Class<T> getExceptionClass(final ExceptionResult exceptionResult) {
        try {
            return (Class<T>) Class.forName(exceptionResult.getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public StackTraceElement[] getStackTrace(final ExceptionResult exceptionResult) {
        try {
            final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(exceptionResult.getStackTrace()));
            return (StackTraceElement[]) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private <I> byte[] readBytes(final String activity, final I input) throws IOException {
        final Request<I> request = new Request<>(input);
        final byte[] payload = SERIALIZER.serialize(request);
        final String baseUrl = clientConfiguration.getBaseUrl();

        HttpPost httpPost = new HttpPost(baseUrl + "/activity?activity=" + activity + "&encoding=" + SerializerTypes.JSON.name());
        httpPost.setEntity(new ByteArrayEntity(payload));
        HttpClient httpClient = HttpClientBuilder.create().build();
        final HttpResponse response = httpClient.execute(httpPost);
        final InputStream s = response.getEntity().getContent();

        return IOUtils.toByteArray(s);
    }
}
