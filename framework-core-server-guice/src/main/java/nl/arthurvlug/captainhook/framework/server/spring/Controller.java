package nl.arthurvlug.captainhook.framework.server.spring;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import nl.arthurvlug.captainhook.framework.common.Input;
import nl.arthurvlug.captainhook.framework.common.Request;
import nl.arthurvlug.captainhook.framework.common.response.FailureResponse;
import nl.arthurvlug.captainhook.framework.common.response.Output;
import nl.arthurvlug.captainhook.framework.common.response.Response;
import nl.arthurvlug.captainhook.framework.common.serialization.Serializer;
import nl.arthurvlug.captainhook.framework.common.serialization.SerializerTypes;
import nl.arthurvlug.captainhook.framework.server.*;
import nl.arthurvlug.captainhook.framework.server.generation.ActivityScanner;
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
import rx.functions.Func1;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableAutoConfiguration
@RestController
@Slf4j
public class Controller {
    @Inject
    AbstractServiceConfiguration serviceConfiguration;

    @Inject
    ApplicationContext applicationContext;

    private ServerActivityPool serverActivityPool;

    @RequestMapping("/")
    public String index() {
        return "The server is online!";
    }

    @RequestMapping("/activity")
    @ResponseBody
    private DeferredResult<byte[]> endpoint(final @RequestParam(name = "activity") String activityName,
                                            final @RequestParam(name = "encoding") String encoding,
                                            final HttpEntity<byte[]> requestEntity) {
        // Create a result that will be populated asynchronously
        final DeferredResult<byte[]> deferredResult = new DeferredResult<>();
        getResponse(activityName, encoding, requestEntity).subscribe(bytes -> deferredResult.setResult(bytes));
        return deferredResult;
    }

    private Observable<byte[]> getResponse(final @RequestParam(name = "activity") String activityName,
                               final @RequestParam(name = "encoding") String encoding,
                               final HttpEntity<byte[]> requestEntity) {
        final Map<String, Object> metadata = new HashMap<>();
        setStartTime(metadata);
        final Serializer serializer = SerializerTypes.getByName(encoding);
        return runActivity(activityName, requestEntity, serializer, metadata)
                .onErrorReturn(t -> failure(t, metadata, activityName))
                .map(new Func1<Response<Output>, byte[]>() { // Can't convert to lamda: compiler will complain about incompatible types
                    @Override
                    public byte[] call(final Response<Output> response) {
                        Controller.this.setEndTime(metadata);
                        Controller.this.setTimeSpentTime(metadata);
                        byte[] bytes = serializer.serialize(response);

                        Controller.this.logResponse(response);
                        return bytes;
                    }
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
        return Response.failure(new RuntimeException("Activity " + activityName + " in server " + serviceConfiguration.getName() + " threw an exception", t), metadata);
    }

    private <I extends Input, O extends Output, RC extends AbstractRequestContext> Observable<Response<O>> runActivity(
                                 final String activityName,
                                 final HttpEntity<byte[]> requestEntity,
                                 final Serializer serializer,
                                 final Map<String, Object> metadata) {
        final ServerActivityConfig<I, O, RC> activityConfig = serverActivityPool.get(activityName);
        Preconditions.checkNotNull(activityConfig, "Activity " + activityName + " could not be found!");

        final TypeToken<Request<I>> requestTypeToken = activityConfig.getRequestTypeToken();
        final Request<I> request = serializer.deserialize(requestEntity.getBody(), requestTypeToken);

        final AbstractActivity<I, O, RC> activity = activityConfig.getActivity();
        return enactActivity(activity, request, metadata);
    }

    private <I extends Input, O extends Output, RC extends AbstractRequestContext> Observable<Response<O>> enactActivity(final AbstractActivity<I, O, RC> activity, final Request<I> request, final Map<String, Object> metadata) {
        final I input = request.getInput();
        final RC requestContext = activity.preActivity(input);
        final Observable<O> activityResult = Observable.defer(() -> activity.enact(input));
        return activityResult.map(output -> {
            activity.postActivity(output, requestContext);
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

    public static void run(Class<? extends AbstractServerSpringComponentsImporter> serverSpringComponentsImporterClass, final String[] args) {
        final AbstractServiceConfiguration clientConfiguration = getServiceConfiguration(serverSpringComponentsImporterClass);

        final List<Class<?>> activityClasses = new ActivityScanner(clientConfiguration).scan();
        final ImmutableList<Class<?>> classes = ImmutableList.<Class<?>>builder()
                .add(serverSpringComponentsImporterClass)
                .addAll(activityClasses)
                .build();

        System.setProperty("server.port", String.valueOf(clientConfiguration.getPort()));
        SpringApplication.run(classes(classes), args);
    }

    private static AbstractServiceConfiguration getServiceConfiguration(final Class<? extends AbstractServerSpringComponentsImporter> serverSpringComponentsImporterClass) {
        final AbstractServerSpringComponentsImporter importer;
        try {
            importer = serverSpringComponentsImporterClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
        return importer.getServiceConfiguration();
    }

    private static Class<?>[] classes(final List<Class<?>> classes) {
        return ImmutableList.<Class>builder()
                .add(Controller.class)
                .addAll(classes)
                .build()
                .toArray(new Class[classes.size() + 1]);
    }

    @PostConstruct
    public void postConstruct() {
        serverActivityPool = new ServerActivityPool(serviceConfiguration);
        applicationContext.getBeansWithAnnotation(Activity.class)
                .values()
                .stream()
                .map(x -> (AbstractActivity) x)
                .forEach(activity -> serverActivityPool.registerActivity(activity));
    }
}
