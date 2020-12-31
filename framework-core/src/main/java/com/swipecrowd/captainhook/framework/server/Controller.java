package com.swipecrowd.captainhook.framework.server;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.swipecrowd.captainhook.framework.common.response.FailureResponse;
import com.swipecrowd.captainhook.framework.common.response.Output;
import com.swipecrowd.captainhook.framework.common.response.Response;
import com.swipecrowd.captainhook.framework.common.serialization.Serializer;
import com.swipecrowd.captainhook.framework.common.serialization.SerializerTypes;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

@EnableAutoConfiguration
@RestController
@Slf4j
public class Controller {
    @Autowired private ApplicationContext applicationContext;
    @Autowired private ServerEndpointComponent serverEndpointComponent;
    @Autowired private AbstractServerProperties serverProperties;

    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public String showConfig() {
        return new Gson().toJson(serverProperties);
    }

    @Operation(hidden = true)
    @RequestMapping("/activity")
    @ResponseBody
    private DeferredResult<byte[]> endpoint(final @RequestParam(name = "activity") String activityName,
                                            final @RequestParam(name = "encoding") String encoding,
                                            final @RequestParam(name = "payload", required = false) String payload,
                                            final HttpSession session,
                                            final HttpEntity<byte[]> requestEntity) {
        // Create a result that will be populated asynchronously
        final DeferredResult<byte[]> deferredResult = new DeferredResult<>();
        byte[] bytes = getResponse(activityName, encoding, payload, requestEntity, session);
        deferredResult.setResult(bytes); // TODO: allow partial responses from the controller, if the activity defines it as an observable
        return deferredResult;
    }

    private byte[] getResponse(final String activityName,
                                           final String encoding,
                                           final String payload,
                                           final HttpEntity<byte[]> requestEntity,
                                           final HttpSession session) {
        final Serializer serializer = SerializerTypes.getByName(encoding);
        final Optional<byte[]> getParamBytes = Optional.ofNullable(payload)
                .map(x -> String.format("{\"input\":%s}", x).getBytes());
        final Optional<byte[]> postParamBytes = Optional.ofNullable(requestEntity.getBody());
        final byte[] payloadBytes = getParamBytes.orElse(postParamBytes.orElse("{}".getBytes()));

        // Can't convert to lambda: compiler will complain about incompatible types
        Response<Output> activityResponse = runActivity(activityName, payloadBytes, serializer, session);
        byte[] bytes = serializer.serialize(activityResponse);

        Controller.this.logResponse(activityResponse);
        return bytes;
    }

    private void logResponse(final Response<Output> response) {
        if(response.getExceptionResult() == null) {
            log.info("Success Response: {}", response);
        } else {
            final Throwable throwable = response.getExceptionResult().convertToThrowable();
            log.error("Something went wrong", throwable);
        }
    }

    private FailureResponse<?> failure(final Throwable t, final String activityName) {
        return Response.failure(new RuntimeException(String.format("Activity %s in server %s threw an exception", activityName, serverEndpointComponent.getServerProperties().getName()), t));
    }

    private <I extends Input, O extends Output, RC extends AbstractRequestContext> Response<O> runActivity(
            final String activityName,
            final byte[] payload,
            final Serializer serializer,
            final HttpSession session) {
        final ServerActivityConfig<I, O, RC> activityConfig = serverEndpointComponent.get(activityName);
        Preconditions.checkNotNull(activityConfig, String.format("Activity %s could not be found!", activityName));

        final IOType<I, O> ioType = activityConfig.getIoType();
        final Request<I> request = serializer.deserialize(payload, ioType.getRequestType());

        final AbstractActivity<I, O, RC> activity = activityConfig.getActivity();
        return enactActivity(activity, request, session);
    }

    /**
     * @throws Exception
     */
    private <I extends Input, O extends Output, RC extends AbstractRequestContext> Response<O> enactActivity(
            final AbstractActivity<I, O, RC> activity,
            final Request<I> request,
            final HttpSession session) {
        try {
            final I input = request.getInput();
            final RC requestContext = activity.preActivity(input);
            O output = activity.handle(input);
            activity.postActivity(input, output, requestContext);
            return Response.success(output);
        } catch (Throwable t) {
            return (Response<O>) failure(t, activity.getClass().getSimpleName());
        }
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

    @PostConstruct
    public void postConstruct() {
        applicationContext.getBeansOfType(AbstractActivity.class)
                .values()
                .stream()
                .map(x -> (AbstractActivity<?, ?, ?>) x)
                .forEach(activity -> {
                    System.out.println("Registering activity " + activity.getClass().getSimpleName());
                    serverEndpointComponent.registerActivity(activity);
                });
    }
}
