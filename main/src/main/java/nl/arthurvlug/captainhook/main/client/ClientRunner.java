package nl.arthurvlug.captainhook.main.client;

import com.arthurvlug.captainhook.examplemiddleservice.activity.merge.MergeInput;
import com.arthurvlug.captainhook.examplemiddleservice.activity.merge.MergeOutput;
import com.arthurvlug.captainhook.examplemiddleservice.clientlib.Client;
import lombok.extern.slf4j.Slf4j;
import nl.arthurvlug.framework.client.DependencyException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@SpringBootApplication
@EnableAutoConfiguration
@Slf4j
@Component
public class ClientRunner {
    private static final DateTimeFormatter formatterOutput = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault());

    private final Client exampleMiddleServiceClient;

    public ClientRunner(@Qualifier("examplemiddleserviceClient") com.arthurvlug.captainhook.examplemiddleservice.clientlib.Client exampleMiddleServiceClient) {
        this.exampleMiddleServiceClient = exampleMiddleServiceClient;
    }

    @PostConstruct
    private void postConstruct() {
        for (int i = 1; ; i++) {
            doCall(i);
        }
    }

    private void doCall(final int iterationNo) {
        log.info("Iteration " + iterationNo + ": Started");
        exampleMiddleServiceClient.mergeCall(MergeInput.builder().name("Iteration " + iterationNo).build())
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<MergeOutput>() {
                    @Override
                    public void onError(final Throwable throwable) {
                        handleError(throwable, iterationNo);
                    }

                    @Override
                    public void onNext(final MergeOutput mergeOutput) {
                        final String outputString = "Message: " + mergeOutput.getMessage() + ",\n"
                                + "Responding time: " + formatterOutput.format(mergeOutput.getRespondingTime());
                        log.info("Iteration " + iterationNo + ": " + outputString);
                    }

                    @Override
                    public void onCompleted() {
                        // Should not happen
                        log.debug("Completed");
                    }
                });
        log.info("Iteration " + iterationNo + ": Going to sleep for 1000ms");
        sleep(1000);
    }

    private void sleep(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e2) {
            throw new RuntimeException(e2);
        }
    }

    private void handleError(final Throwable e, final int iterationNo) {
        if (e.getCause() instanceof DependencyException) {
            log.error("Iteration " + iterationNo + ": Dependency threw an exception: " + e.getCause().getMessage());
        } else {
            log.error("Iteration " + iterationNo + ": Unexpected exception: " + e);
        }
    }
}
