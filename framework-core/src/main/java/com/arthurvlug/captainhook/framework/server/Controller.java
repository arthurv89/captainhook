package com.arthurvlug.captainhook.framework.server;

import com.arthurvlug.captainhook.framework.common.response.FailureResponse;
import com.arthurvlug.captainhook.framework.common.response.Output;
import com.arthurvlug.captainhook.framework.common.response.Response;
import com.arthurvlug.captainhook.framework.common.serialization.Serializer;
import com.arthurvlug.captainhook.framework.common.serialization.SerializerTypes;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EnableAutoConfiguration
@RestController
@Slf4j
public class Controller {
    @Autowired
    private ApplicationContext applicationContext;

    private static AbstractServerEndpointComponent serverEndpointComponent = null;

    @RequestMapping("/")
    public String index() {
        return "The server is online!";
    }

    @RequestMapping("/activity")
    @ResponseBody
    private DeferredResult<byte[]> endpoint(final @RequestParam(name = "activity") String activityName,
                                            final @RequestParam(name = "encoding") String encoding,
                                            final @RequestParam(name = "payload", required = false) String payload,
                                            final HttpEntity<byte[]> requestEntity) {
        // Create a result that will be populated asynchronously
        final DeferredResult<byte[]> deferredResult = new DeferredResult<>();
        getResponse(activityName, encoding, payload, requestEntity).subscribe(bytes -> deferredResult.setResult(bytes));
        return deferredResult;
    }

    private Observable<byte[]> getResponse(
            final @RequestParam(name = "activity") String activityName,
            final @RequestParam(name = "encoding") String encoding,
            final @RequestParam(name = "payload", required = false) String payload,
            final HttpEntity<byte[]> requestEntity) {
        final Map<String, Object> metadata = new HashMap<>();
        setStartTime(metadata);
        final Serializer serializer = SerializerTypes.getByName(encoding);
        final Optional<byte[]> getParamBytes = Optional.ofNullable(payload)
                .map(x -> String.format("{\"input\":%s}", x).getBytes());
        final Optional<byte[]> postParamBytes = Optional.ofNullable(requestEntity.getBody());
        final byte[] payloadBytes = getParamBytes.orElse(postParamBytes.orElse("{}".getBytes()));
        // Can't convert to lambda: compiler will complain about incompatible types
        return runActivity(activityName, payloadBytes, serializer, metadata)
                .onErrorReturn(t -> failure(t, metadata, activityName))
                .map(response -> {
                    Controller.this.setEndTime(metadata);
                    Controller.this.setTimeSpentTime(metadata);
                    byte[] bytes = serializer.serialize(response);

                    Controller.this.logResponse((Response<Output>) response);
                    return bytes;
                });
    }

    private void logResponse(final Response<Output> response) {
        if(response.getExceptionResult() == null) {
            log.info("Success Response: {}", response);
        } else {
            final Throwable throwable = response.getExceptionResult().convertToThrowable();
            log.error("Something went wrong", throwable);
        }
    }

    private FailureResponse failure(final Throwable t, final Map<String, Object> metadata, final String activityName) {
        return Response.failure(new RuntimeException(String.format("Activity %s in server %s threw an exception", activityName, serverEndpointComponent.getServerProperties().getServerName()), t), metadata);
    }

    private <I extends Input, O extends Output, RC extends AbstractRequestContext> Observable<Response<O>> runActivity(
                                 final String activityName,
                                 final byte[] payload,
                                 final Serializer serializer,
                                 final Map<String, Object> metadata) {
        final ServerActivityConfig<I, O, RC> activityConfig = serverEndpointComponent.get(activityName);
        Preconditions.checkNotNull(activityConfig, String.format("Activity %s could not be found!", activityName));

        final IOType<I, O> ioType = activityConfig.getIoType();
        final Request<I> request = serializer.deserialize(payload, ioType.getRequestType());

        final AbstractActivity<I, O, RC> activity = activityConfig.getActivity();
        return enactActivity(activity, request, metadata);
    }

    private <I extends Input, O extends Output, RC extends AbstractRequestContext> Observable<Response<O>> enactActivity(final AbstractActivity<I, O, RC> activity, final Request<I> request, final Map<String, Object> metadata) {
        final I input = request.getInput();
        final RC requestContext = activity.preActivity(input);
        final Observable<O> activityResult = Observable.defer(() -> activity.enact(input));
        return activityResult.map(output -> {
            activity.postActivity(input, output, requestContext);
            return Response.success(output, metadata);
        });
    }

    private void setStartTime(final Map<String, Object> map) {
        map.put("startTime", System.nanoTime());
    }

    private void setEndTime(final Map<String, Object> map) {
        map.put("endTime", System.nanoTime());
    }

    private void setTimeSpentTime(final Map<String, Object> map) {
        final long timeSpent = (long) map.get("endTime") - (long) map.get("startTime");
        map.put("timeSpent", timeSpent);
    }

    private static Class<?>[] classes(final Collection<Class<?>> classes) {
        return ImmutableList.<Class>builder()
                .add(Controller.class)
                .addAll(classes)
                .build()
                .toArray(new Class[classes.size() + 1]);
    }

    public static void run(final AbstractServerProperties serverProperties, final String[] args) {
        run(serverProperties, ImmutableList.of(), args);
    }

    public static void run(final AbstractServerProperties serverProperties, Plugin pluginControllers, final String[] args) {
        run(serverProperties, ImmutableList.of(pluginControllers), args);
    }

    public static void run(final AbstractServerProperties serverProperties, final List<Plugin> pluginControllers, final String[] args) {
        serverEndpointComponent = new ServerEndpointComponent(serverProperties);

        System.setProperty("server.port", String.valueOf(serverEndpointComponent.getServerProperties().getPort()));

        SpringApplication.run(
                getClasses(pluginControllers),
                args);
    }

    private static Class<?>[] getClasses(final List<Plugin> pluginControllers) {
        final List<Class<?>> activityClasses = new ActivityScanner(serverEndpointComponent.getServerProperties())
                .scan();

        final List<Class<?>> pluginClasses = pluginControllers.stream().flatMap(x -> x.getClasses().stream()).collect(Collectors.toList());
        final Collection<Class<?>> classSet = ImmutableSet.<Class<?>>builder()
                .addAll(pluginClasses)
                .addAll(activityClasses)
                .build();
        return classes(classSet);
    }

    @PostConstruct
    public void postConstruct() {
        applicationContext.getBeansWithAnnotation(Activity.class)
                .values()
                .stream()
                .map(x -> (AbstractActivity<?, ?, ?>) x)
                .forEach(activity -> serverEndpointComponent.registerActivity(activity));
    }
}
