package nl.arthurvlug.captainhook.framework.client;

import com.google.gson.reflect.TypeToken;
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

import java.io.IOException;

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
                        final Throwable throwable = response.getExceptionResult().convertToThrowable();
                        return Observable.error(throwable);
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

    protected abstract String getName();
}
