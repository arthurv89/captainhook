package nl.arthurvlug.captainhook.framework.server;

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
public abstract class Controller {
    @Autowired
    private AbstractServerActivityPool serverActivityPool;

    @RequestMapping("/")
    @ResponseBody
    private byte[] endpoint(final @RequestParam(name = "activity") String activityName,
                            final @RequestParam(name = "encoding") String encoding,
                            final HttpEntity<byte[]> requestEntity) {
        final ServerActivityConfig activityConfig = serverActivityPool.get(activityName);

        final Serializer serializer = SerializerTypes.getByName(encoding);
        final Request<? extends Input> request = (Request<? extends Input>) serializer.deserialize(requestEntity.getBody(), activityConfig.getRequestTypeToken());

        final Response response = enactActivity(activityConfig.getActivity(), request);
        return serializer.serialize(response);

    }

    private Response enactActivity(final AbstractActivity activity, final Request<? extends Input> request) {
        try {
            final Input input = request.getInput();
            final AbstractRequestContext requestContext = activity.preActivity(input);
            final Output output = activity.enact(input);
            activity.postActivity(output, requestContext);
            return Response.success(output);
        } catch (Exception e) {
            return Response.failure(e);
        }
    }
}
