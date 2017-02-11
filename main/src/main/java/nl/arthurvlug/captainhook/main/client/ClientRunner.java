package nl.arthurvlug.captainhook.main.client;

import com.arthurvlug.captainhook.examplemiddleservice.activity.merge.MergeInput;
import com.arthurvlug.captainhook.examplemiddleservice.activity.merge.MergeOutput;
import lombok.extern.slf4j.Slf4j;
import nl.arthurvlug.captainhook.framework.client.AbstractClientRunner;
import nl.arthurvlug.captainhook.framework.common.response.DependencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import rx.Subscriber;
import rx.schedulers.Schedulers;

@Slf4j
@Component
public class ClientRunner extends AbstractClientRunner {
    @Autowired
    @Qualifier("examplemiddleserviceClient")
    private com.arthurvlug.captainhook.examplemiddleservice.client.Client exampleMiddleService;

    @Override
    public void run() {
        for (int i = 1; ; i++) {
            doCall(i);
        }
    }

    private void doCall(final int iterationNo) {
        log.info("Iteration " + iterationNo + ": Started");
        exampleMiddleService.mergeCall(MergeInput.builder().name("Iteration " + iterationNo).build())
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<MergeOutput>() {
                    @Override
                    public void onError(final Throwable throwable) {
                        handleError(throwable, iterationNo);
                    }

                    @Override
                    public void onNext(final MergeOutput mergeOutput) {
                        log.info("Iteration " + iterationNo + ": " + String.valueOf(mergeOutput));
                    }

                    @Override
                    public void onCompleted() {
                        // Should not happen
                        log.debug("Completed");
                    }
                });
        log.info("Iteration " + iterationNo + ": Goint to sleep for 1000ms");
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
            log.error("Iteration " + iterationNo + ": Dependency threw an exception: " + e.getCause().getClass().getName() + ": " + e.getCause().getMessage());
        } else {
            log.error("Iteration " + iterationNo + ": Unexpected exception: " + e.getCause().getClass().getName() + ": " + e.getCause().getMessage());
        }
    }
}
