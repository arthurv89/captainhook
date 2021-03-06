package com.swipecrowd.captainhook.framework.application.common;

import com.google.gson.reflect.TypeToken;
import com.swipecrowd.captainhook.framework.application.client.DependencyProperties;
import com.swipecrowd.captainhook.framework.application.common.response.Output;
import com.swipecrowd.captainhook.framework.application.common.response.Response;
import com.swipecrowd.captainhook.framework.application.common.serialization.Serializer;
import com.swipecrowd.captainhook.framework.application.common.serialization.SerializerTypes;
import com.swipecrowd.captainhook.framework.application.server.AbstractServerProperties;
import lombok.AllArgsConstructor;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import io.reactivex.rxjava3.core.Observable;

@AllArgsConstructor
public abstract class AbstractClient {
    private static final Serializer SERIALIZER = SerializerTypes.JSON.getSerializer();

    private static final AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
    public final AbstractServerProperties serverProperties;

    protected <I extends Input, O extends Output> Observable<O> createCall(final String serviceName,
                                                                           final String activity,
                                                                           final I input,
                                                                           final TypeToken<Response<O>> outputTypeToken) {
        try {
            return readBytes(serviceName, activity, input).flatMap(bytes -> {
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

    private <I extends Input> Observable<byte[]> readBytes(final String serviceName,
                                                           final String activity,
                                                           final I input) {
        final Request<I> request = new Request<>(input);
        final byte[] payload = SERIALIZER.serialize(request);
        final DependencyProperties dependencyServerProperties = new DependencyProperties(serverProperties, serviceName);
        final String baseUrl = "http://" + dependencyServerProperties.getHost() + ":" + dependencyServerProperties.getPort();

        final String url = String.format("%s/activity?activity=%s&encoding=%s", baseUrl, activity, SerializerTypes.JSON.name());
        final ListenableFuture<org.asynchttpclient.Response> response = asyncHttpClient.preparePost(url)
                .setBody(payload)
                .execute();
        final Observable<org.asynchttpclient.Response> obs = Observable.fromFuture(response);
        return obs.map(x -> x.getResponseBodyAsBytes());
    }
}
