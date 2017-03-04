package nl.arthurvlug.captainhook.framework.server.spring;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import nl.arthurvlug.captainhook.framework.server.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@EnableAutoConfiguration
@RestController
@Slf4j
public class CaptainHookSpringController {
    @Inject
    private ApplicationContext applicationContext;

    @Inject
    private RequestHandler requestHandler;

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
        requestHandler.getResponse(activityName, encoding, requestEntity.getBody())
                .subscribe(bytes -> deferredResult.setResult(bytes));
        return deferredResult;
    }

    public static void run(final Class<? extends AbstractServerComponentsImporter> serverSpringComponentsImporterClass, final String[] args) {
        final String packageName = RequestHandler.getServiceConfiguration(serverSpringComponentsImporterClass).getPackageName();

        final List<Class<? extends AbstractActivity>> activityClasses = RequestHandler.activityClasses(packageName);
        final ImmutableList<Class<?>> classes = ImmutableList.<Class<?>>builder()
                .add(CaptainHookSpringController.class)
                .add(RequestHandler.class)
                .add(serverSpringComponentsImporterClass)
                .addAll(activityClasses)
                .build();
        final Class<?>[] classArray = classes.toArray(new Class<?>[classes.size()]);
        SpringApplication.run(classArray, args);
    }

    @PostConstruct
    public void postConstruct() {
        requestHandler.init(findActivities());
    }

    private List<AbstractActivity> findActivities() {
        return applicationContext.getBeansWithAnnotation(Activity.class)
                    .values()
                    .stream()
                    .map(x -> (AbstractActivity) x)
                    .collect(Collectors.toList());
    }
}
