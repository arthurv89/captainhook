package nl.arthurvlug.captainhook.framework.server;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import nl.arthurvlug.captainhook.framework.common.response.Output;
import nl.arthurvlug.captainhook.framework.common.response.Response;
import nl.arthurvlug.captainhook.framework.common.serialization.Serializer;
import nl.arthurvlug.captainhook.framework.common.serialization.SerializerTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
        final Serializer serializer = SerializerTypes.getByName(encoding);
        try {
            final ServerActivityConfig activityConfig = serverActivityPool.get(activityName);
            Preconditions.checkNotNull(activityConfig, "Activity " + activityName + " could not be found!");

            final Request<? extends Input> request = (Request<? extends Input>) serializer.deserialize(requestEntity.getBody(), activityConfig.getRequestTypeToken());

            final Response response = enactActivity(activityConfig.getActivity(), request);
            return serializer.serialize(response);
        } catch (Throwable e)  {
            log.error("Error in request to activity {}", activityName, e);
            final Response response = Response.failure(e);
            return serializer.serialize(response);
        }
    }

    private Response enactActivity(final AbstractActivity activity, final Request<? extends Input> request) throws Exception {
        final Input input = request.getInput();
        final AbstractRequestContext requestContext = activity.preActivity(input);
        final Output output = activity.enact(input);
        activity.postActivity(output, requestContext);
        return Response.success(output);
    }
}
