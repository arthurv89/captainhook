package com.swipecrowd.captainhook.framework.server;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.swipecrowd.captainhook.framework.common.response.FailureResponse;
import com.swipecrowd.captainhook.framework.common.response.Output;
import com.swipecrowd.captainhook.framework.common.response.Response;
import com.swipecrowd.captainhook.framework.common.serialization.Serializer;
import com.swipecrowd.captainhook.framework.common.serialization.SerializerTypes;
import com.swipecrowd.captainhook.framework.generation.clientlib.GenerateClientLibClasses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.security.SecureClassLoader;
import java.util.*;
import java.util.stream.Collectors;

@EnableAutoConfiguration
@RestController
@Slf4j
public class Controller {
    @Autowired
    private ApplicationContext applicationContext;

    private static AbstractServerEndpointComponent serverEndpointComponent;

    @RequestMapping("/")
    public String index() {
        return "The server is online!";
    }

    @RequestMapping("/activity")
    @ResponseBody
    private DeferredResult<byte[]> endpoint(final @RequestParam(name = "activity") String activityName,
                                            final @RequestParam(name = "encoding") String encoding,
                                            final @RequestParam(name = "payload", required = false) String payload,
                                            final HttpSession session,
                                            final HttpEntity<byte[]> requestEntity) {
        // Create a result that will be populated asynchronously
        final DeferredResult<byte[]> deferredResult = new DeferredResult<>();
        getResponse(activityName, encoding, payload, requestEntity, session).subscribe(bytes -> deferredResult.setResult(bytes));
        return deferredResult;
    }

    private Observable<byte[]> getResponse(final String activityName,
                                           final String encoding,
                                           final String payload,
                                           final HttpEntity<byte[]> requestEntity,
                                           final HttpSession session) {
        final Map<String, Object> metadata = new HashMap<>();
        setStartTime(metadata);
        final Serializer serializer = SerializerTypes.getByName(encoding);
        final Optional<byte[]> getParamBytes = Optional.ofNullable(payload)
                .map(x -> String.format("{\"input\":%s}", x).getBytes());
        final Optional<byte[]> postParamBytes = Optional.ofNullable(requestEntity.getBody());
        final byte[] payloadBytes = getParamBytes.orElse(postParamBytes.orElse("{}".getBytes()));

        // Can't convert to lambda: compiler will complain about incompatible types
        return runActivity(activityName, payloadBytes, serializer, metadata, session)
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
        return Response.failure(new RuntimeException(String.format("Activity %s in server %s threw an exception", activityName, getServerName()), t), metadata);
    }

    private <I extends Input, O extends Output, RC extends AbstractRequestContext> Observable<Response<O>> runActivity(
            final String activityName,
            final byte[] payload,
            final Serializer serializer,
            final Map<String, Object> metadata,
            final HttpSession session) {
        final ServerActivityConfig<I, O, RC> activityConfig = serverEndpointComponent.get(activityName);
        Preconditions.checkNotNull(activityConfig, String.format("Activity %s could not be found!", activityName));

        final IOType<I, O> ioType = activityConfig.getIoType();
        final Request<I> request = serializer.deserialize(payload, ioType.getRequestType());

        final AbstractActivity<I, O, RC> activity = activityConfig.getActivity();
        return enactActivity(activity, request, metadata, session);
    }

    private <I extends Input, O extends Output, RC extends AbstractRequestContext> Observable<Response<O>> enactActivity(
            final AbstractActivity<I, O, RC> activity,
            final Request<I> request,
            final Map<String, Object> metadata,
            final HttpSession session) {
        final I input = request.getInput();
        final RC requestContext = activity.preActivity(input);
        final ActivityRequest<I> activityRequest = new ActivityRequest<I>(input, session);
        final Observable<O> activityResult = Observable.defer(() -> activity.handle(activityRequest));
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

    public static ConfigurableApplicationContext run(
            final AbstractGeneratedServerProperties generatedServerProperties,
            final Class<? extends AbstractServerProperties> serverPropertiesClass,
            final String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        final Map<String, String> allProperties = mergeProperties(args);
        final AbstractServerProperties serverProperties = createServerProperties(serverPropertiesClass, allProperties);
        return run(generatedServerProperties, serverProperties, DefaultServerConfiguration.class, args);
    }

    private static AbstractServerProperties createServerProperties(final Class<? extends AbstractServerProperties> serverPropertiesClass, final Map<String, String> allProperties) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return serverPropertiesClass.getDeclaredConstructor(Map.class).newInstance(allProperties);
    }

    private static Map<String, String> mergeProperties(final String[] args) throws IOException {
        final Map<String, String> map = new HashMap<>();
        map.putAll(toMap(args));
        map.putAll(toMap(getProperties()));
        return map;
    }

    private static Map<String, String> toMap(final Properties properties) {
        final Map<String, String> map = new HashMap<>();
        for (final String name: properties.stringPropertyNames()) {
            map.put(name, properties.getProperty(name));
        }
        return map;
    }

    private static Properties getProperties() throws IOException {
        final SecureClassLoader classLoader = (SecureClassLoader) GenerateClientLibClasses.class.getClassLoader();
        final String propertiesFile = "application.properties";

        final InputStream resourceAsStream = classLoader.getResourceAsStream(propertiesFile);

        final Properties properties = new Properties();
        properties.load(resourceAsStream);
        return properties;
    }

    public static ConfigurableApplicationContext run(
            final AbstractGeneratedServerProperties generatedServerProperties,
            final AbstractServerProperties serverProperties,
            final Class<? extends ServerConfiguration> serverConfiguration,
            final String[] args) {
        serverEndpointComponent = new ServerEndpointComponent(generatedServerProperties, serverProperties);

        System.setProperty("server.port", String.valueOf(serverProperties.getPort()));
        return MyApplication.start(serverProperties, generatedServerProperties, getSources(serverConfiguration), args);
    }

    private static Object[] getSources(final Class<? extends ServerConfiguration> serverConfigurationClass) {
        final Collection<Class<?>> activityClasses = new ActivityScanner(serverEndpointComponent.getGeneratedServerProperties())
                .scan();

        final Collection<Class<?>> classSet = ImmutableSet.<Class<?>>builder()
                .add(serverConfigurationClass)
                .addAll(activityClasses)
                .build();
        return beans(classSet);
    }

    private static Object[] beans(final Collection<Class<?>> classes) {
        return ImmutableList.builder()
                .add(MyApplication.class)
                .add(Controller.class)
                .addAll(classes)
                .build()
                .toArray(new Object[classes.size() + 1]);
    }

    private String getServerName() {
        return serverEndpointComponent.getServerProperties().getHost();
    }

    private static int getPort() {
        return serverEndpointComponent.getServerProperties().getPort();
    }

    private static Map<String, String> toMap(final String[] args) {
        return Arrays.stream(args)
                .map(x -> x.split("="))
                .collect(Collectors.toMap(
                        arr -> arr[0].substring(2),
                        arr -> arr[1]));
    }

    @PostConstruct
    public void postConstruct() {
        applicationContext.getBeansWithAnnotation(Activity.class)
                .values()
                .stream()
                .map(x -> (AbstractActivity<?, ?, ?>) x)
                .forEach(activity -> {
                    System.out.println("Registering activity " + activity.getClass().getSimpleName());
                    serverEndpointComponent.registerActivity(activity);
                });
    }
}
