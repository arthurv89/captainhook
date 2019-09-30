package com.arthurvlug.captainhook.main.client;

import com.arthurvlug.captainhook.examplemiddleservice.activity.merge.MergeInput;
import com.arthurvlug.captainhook.examplemiddleservice.activity.merge.MergeOutput;
import com.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldInput;
import com.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldOutput;
import com.arthurvlug.captainhook.framework.client.AbstractClientRunner;
import com.arthurvlug.captainhook.framework.common.response.DependencyException;
import lombok.extern.slf4j.Slf4j;
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

    private com.arthurvlug.captainhook.examplemiddleservice.client.Client mergeServiceClient = new com.arthurvlug.captainhook.examplemiddleservice.client.Client();
    private com.arthurvlug.captainhook.exampleservice.client.Client exampleService1Client = new com.arthurvlug.captainhook.exampleservice.client.Client();

    @Override
    public void run() {
        for (int i = 1; ; i++) {
            doCall(i);
        }
    }

    private void doCall(final int iterationNo) {
        log.info("Iteration " + iterationNo + ": Started");
        callMergeService(iterationNo);
//        callService1(iterationNo);
        log.info("Iteration " + iterationNo + ": Going to sleep for 1000ms");
        sleep(1000);
    }

    private void callService1(final int iterationNo) {
        HelloWorldInput input = HelloWorldInput.builder().name("Iteration " + iterationNo).build();
        exampleService1Client.helloWorldCall(input)
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

    }

    private void callMergeService(final int iterationNo) {
        MergeInput input = MergeInput.builder().name("Iteration " + iterationNo).build();
        mergeServiceClient.mergeCall(input)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<MergeOutput>() {
                    @Override
                    public void onError(final Throwable throwable) {
                        handleError(throwable, iterationNo);
                    }

                    @Override
                    public void onNext(final MergeOutput helloworldOutput) {
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
