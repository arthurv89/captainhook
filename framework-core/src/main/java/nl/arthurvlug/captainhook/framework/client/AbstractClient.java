package nl.arthurvlug.captainhook.framework.client;

import com.google.gson.reflect.TypeToken;
import nl.arthurvlug.captainhook.framework.common.response.DependencyException;
import nl.arthurvlug.captainhook.framework.common.response.ExceptionResult;
import nl.arthurvlug.captainhook.framework.common.response.Output;
import nl.arthurvlug.captainhook.framework.common.response.Response;
import nl.arthurvlug.captainhook.framework.common.serialization.Serializer;
import nl.arthurvlug.captainhook.framework.common.serialization.SerializerTypes;
import nl.arthurvlug.captainhook.framework.server.Input;
import nl.arthurvlug.captainhook.framework.server.Request;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import rx.Observable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractClient {
    private static final Serializer SERIALIZER = SerializerTypes.JSON.getSerializer();

    private AbstractClientActivityPool clientActivityPool;

    private AbstractClientConfiguration clientConfiguration;

    private static final AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();

    protected AbstractClient(final AbstractClientActivityPool clientActivityPool,
                             final AbstractClientConfiguration clientConfiguration) {
        this.clientActivityPool = clientActivityPool;
        this.clientConfiguration = clientConfiguration;
    }

    protected <I extends Input, O extends Output> Observable<O> createCall(final String activity,
                                                                           final I input) {
        try {
            return readBytes(activity, input).flatMap(bytes -> {
                try {
                    final TypeToken<Response<O>> outputTypeToken = getResponseTypeToken(activity);
                    final Response<O> response = SERIALIZER.deserialize(bytes, outputTypeToken);
                    if (response.getExceptionResult() == null) {
                        return Observable.just(response.getValue());
                    } else {
                        final ExceptionResult exceptionResult = response.getExceptionResult();
                        final Exception e = createException(exceptionResult);
                        return Observable.error(dependencyException(e));
                    }
                } catch (Exception e) {
                    return Observable.error(e);
                }
            });
        } catch (Exception e) {
            return Observable.error(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <I extends Input, O extends Output> TypeToken<Response<O>> getResponseTypeToken(final String activity) {
        final ClientActivityConfig<I, O> inputOutputClientActivityConfig = clientActivityPool.get(activity);
        return inputOutputClientActivityConfig.getResponseTypeToken();
    }

    private Exception createException(final ExceptionResult exceptionResult) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        final Exception exception = getExceptionClass(exceptionResult)
                .getConstructor(String.class)
                .newInstance(exceptionResult.getMessage());
        exception.setStackTrace(getStackTrace(exceptionResult));
        return exception;
    }

    @SuppressWarnings("unchecked")
    public <T extends Exception> Class<T> getExceptionClass(final ExceptionResult exceptionResult) throws ClassNotFoundException {
        return (Class<T>) Class.forName(exceptionResult.getClassName());
    }

    public StackTraceElement[] getStackTrace(final ExceptionResult exceptionResult) throws IOException, ClassNotFoundException {
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(exceptionResult.getStackTrace()));
        return (StackTraceElement[]) ois.readObject();
    }

    private <I extends Input> Observable<byte[]> readBytes(final String activity, final I input) throws IOException {
        final Request<I> request = new Request<>(input);
        final byte[] payload = SERIALIZER.serialize(request);
        final String baseUrl = clientConfiguration.getBaseUrl();

        final String url = baseUrl + "/activity?activity=" + activity + "&encoding=" + SerializerTypes.JSON.name();
        final ListenableFuture<org.asynchttpclient.Response> response = asyncHttpClient.preparePost(url)
                .setBody(payload)
                .execute();
        final Observable<org.asynchttpclient.Response> obs = Observable.from(response);
        return obs.map(x -> x.getResponseBodyAsBytes());
    }

    private DependencyException dependencyException(final Exception e) {
        return new DependencyException(e, "Server " + getName() + "  threw an exception");
    }

    protected abstract String getName();
}
