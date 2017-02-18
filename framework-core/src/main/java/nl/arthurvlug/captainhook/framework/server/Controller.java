package nl.arthurvlug.captainhook.framework.server;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import nl.arthurvlug.captainhook.framework.common.response.FailureResponse;
import nl.arthurvlug.captainhook.framework.common.response.Output;
import nl.arthurvlug.captainhook.framework.common.response.Response;
import nl.arthurvlug.captainhook.framework.common.serialization.Serializer;
import nl.arthurvlug.captainhook.framework.common.serialization.SerializerTypes;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableAutoConfiguration
@RestController
@Slf4j
public class Controller {
    @Autowired
    ServerActivityPool serverActivityPool;

    @Autowired
    ApplicationContext applicationContext;

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
        getResponse(activityName, encoding, requestEntity)
                .subscribe(bytes -> deferredResult.setResult(bytes));
        return deferredResult;
    }

    private Observable<byte[]> getResponse(final @RequestParam(name = "activity") String activityName,
                               final @RequestParam(name = "encoding") String encoding,
                               final HttpEntity<byte[]> requestEntity) {
        final Map<String, Object> metadata = new HashMap<>();
        setStartTime(metadata);
        final Serializer serializer = SerializerTypes.getByName(encoding);
        return activityResult(activityName, requestEntity, serializer, metadata)
                .onErrorReturn(t -> failure(t, metadata))
                .map(response -> {
                    setEndTime(metadata);
                    setTimeSpentTime(metadata);
                    byte[] bytes = serializer.serialize(response);

                    log.info("Response: {}", response);

                    return bytes;
                });
    }

    private FailureResponse failure(final Throwable t, final Map<String, Object> metadata) {
        return Response.failure(t, metadata);
    }

    private <O extends Output> Observable<Response<O>> activityResult(final String activityName, final HttpEntity<byte[]> requestEntity, final Serializer serializer, final Map<String, Object> metadata) {
        try {
            return runActivity(activityName, requestEntity, serializer, metadata);
        } catch (Throwable e) {
            return Observable.error(e);
        }
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
        final Observable<O> activityResult = activity.enact(input);
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
        final AbstractServerSpringComponentsImporter x;
        try {
            x = serverSpringComponentsImporterClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
        final AbstractCommonConfiguration commonConfiguration = x.getCommonConfiguration();


        final List<Class<?>> activityClasses = new ActivityScanner(commonConfiguration)
                .scan();
        final ImmutableList<Class<?>> classes = ImmutableList.<Class<?>>builder()
                .add(serverSpringComponentsImporterClass)
                .addAll(activityClasses)
                .build();

        SpringApplication.run(classes(classes), args);
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
        applicationContext.getBeansWithAnnotation(Activity.class)
                .values()
                .stream()
                .map(x -> (AbstractActivity) x)
                .forEach(activity -> serverActivityPool.registerActivity(activity));
    }
}
