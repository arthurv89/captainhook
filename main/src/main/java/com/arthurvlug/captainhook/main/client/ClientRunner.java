package com.arthurvlug.captainhook.main.client;

import com.arthurvlug.captainhook.examplemiddleservice.activity.merge.MergeInput;
import com.arthurvlug.captainhook.examplemiddleservice.activity.merge.MergeOutput;
import com.arthurvlug.captainhook.examplemiddleservice.client.Client;
import lombok.extern.slf4j.Slf4j;
import com.arthurvlug.captainhook.framework.client.AbstractClientRunner;
import com.arthurvlug.captainhook.framework.common.response.DependencyException;
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

    private com.arthurvlug.captainhook.examplemiddleservice.client.Client mergeService = new Client();

    @Override
    public void run() {
        for (int i = 1; ; i++) {
            doCall(i);
        }
    }

    private void doCall(final int iterationNo) {
        log.info("Iteration " + iterationNo + ": Started");
        MergeInput input = MergeInput.builder().name("Iteration " + iterationNo).build();
        mergeService.mergeCall(input)
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