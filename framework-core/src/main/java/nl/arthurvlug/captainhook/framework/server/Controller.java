package nl.arthurvlug.captainhook.framework.server;

import com.google.common.base.Preconditions;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import nl.arthurvlug.captainhook.framework.common.response.*;
import nl.arthurvlug.captainhook.framework.common.serialization.Serializer;
import nl.arthurvlug.captainhook.framework.common.serialization.SerializerTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@EnableAutoConfiguration
@Slf4j
public abstract class Controller {
    @Autowired
    private AbstractServerActivityPool serverActivityPool;

    @Autowired
    private ActivityScanner activityScanner;

    @RequestMapping("/")
    public String index() {
        return "The server is online!";
    }


    @RequestMapping("/activity")
    @ResponseBody
    private byte[] endpoint(final @RequestParam(name = "activity") String activityName,
                            final @RequestParam(name = "encoding") String encoding,
                            final HttpEntity<byte[]> requestEntity) {
        final Map<String, Object> metadata = new HashMap<>();
        setStartTime(metadata);
        final Serializer serializer = SerializerTypes.getByName(encoding);
        final Response<?> response = runActivity(activityName, requestEntity, serializer, metadata);
        setEndTime(metadata);
        setTimeSpentTime(metadata);
        log.info("Response: {}", response);

        return serializer.serialize(response);
    }

    private <I extends Input, O extends Output, RC extends AbstractRequestContext> Response<O> runActivity(
                                 final String activityName,
                                 final HttpEntity<byte[]> requestEntity,
                                 final Serializer serializer,
                                 final Map<String, Object> metadata) {
        try {
            final ServerActivityConfig<I, O, RC> activityConfig = serverActivityPool.get(activityName);
            Preconditions.checkNotNull(activityConfig, "Activity " + activityName + " could not be found!");

            final TypeToken<Request<I>> requestTypeToken = activityConfig.getRequestTypeToken();
            final Request<I> request = serializer.deserialize(requestEntity.getBody(), requestTypeToken);

            final AbstractActivity<I, O, RC> activity = activityConfig.getActivity();
            return enactActivity(activity, request, metadata);
        } catch (Throwable e) {
            log.error("Error in request to activity {}", activityName, e);
            return (Response<O>) FailureResponse.failure(e, metadata);
        }
    }

    private <I extends Input, O extends Output, RC extends AbstractRequestContext> SuccessResponse<O> enactActivity(final AbstractActivity<I, O, RC> activity, final Request<I> request, final Map<String, Object> metadata) throws Exception {
        final I input = request.getInput();
        final RC requestContext = activity.preActivity(input);
        final O output = activity.enact(input);
        activity.postActivity(output, requestContext);
        return Response.success(output, metadata);
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
}
