package nl.arthurvlug.captainhook.main.client;

import nl.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldInput;
import nl.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldOutput;
import lombok.extern.slf4j.Slf4j;
import nl.arthurvlug.captainhook.framework.client.AbstractClientRunner;
import nl.arthurvlug.captainhook.framework.common.response.DependencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Slf4j
@Component
public class ClientRunner extends AbstractClientRunner {
    private static final DateTimeFormatter formatterOutput = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault());

    @Autowired
    @Qualifier("exampleserviceClient")
    private nl.arthurvlug.captainhook.exampleservice.client.Client exampleService;

    @Override
    public void run() {
        for (int i = 1; ; i++) {
            doCall(i);
        }
    }

    private void doCall(final int iterationNo) {
        log.info("Iteration " + iterationNo + ": Started");
        exampleService.helloWorldCall(HelloWorldInput.builder().name("Iteration " + iterationNo).build())
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<HelloWorldOutput>() {
                    @Override
                    public void onError(final Throwable throwable) {
                        handleError(throwable, iterationNo);
                    }

                    @Override
                    public void onNext(final HelloWorldOutput helloworldOutput) {
                        final String outputString = "Message: " + helloworldOutput.getMessage() + ",\n"
                                + "Responding time: " + formatterOutput.format(helloworldOutput.getRespondingTime());
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
