package com.arthurvlug.captainhook.framework.client;

import com.arthurvlug.captainhook.framework.common.response.Output;
import com.arthurvlug.captainhook.framework.common.response.Response;
import com.arthurvlug.captainhook.framework.common.serialization.Serializer;
import com.arthurvlug.captainhook.framework.common.serialization.SerializerTypes;
import com.arthurvlug.captainhook.framework.server.Input;
import com.arthurvlug.captainhook.framework.server.Request;
import com.google.gson.reflect.TypeToken;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import rx.Observable;

public abstract class AbstractClient {
    private static final Serializer SERIALIZER = SerializerTypes.JSON.getSerializer();

    private static final AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();

    protected <I extends Input, O extends Output> Observable<O> createCall(final String activity,
                                                                           final I input,
                                                                           final TypeToken<Response<O>> outputTypeToken) {
        try {
            return readBytes(activity, input).flatMap(bytes -> {
                try {
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

    private <I extends Input> Observable<byte[]> readBytes(final String activity, final I input) {
        final Request<I> request = new Request<>(input);
        final byte[] payload = SERIALIZER.serialize(request);
        final String baseUrl = getBaseUrl();

        final String url = baseUrl + "/activity?activity=" + activity + "&encoding=" + SerializerTypes.JSON.name();
        final ListenableFuture<org.asynchttpclient.Response> response = asyncHttpClient.preparePost(url)
                .setBody(payload)
                .execute();
        final Observable<org.asynchttpclient.Response> obs = Observable.from(response);
        return obs.map(x -> x.getResponseBodyAsBytes());
    }

    protected abstract String getBaseUrl();
}
