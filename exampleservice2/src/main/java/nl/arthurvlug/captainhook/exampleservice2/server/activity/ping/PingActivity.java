package nl.arthurvlug.captainhook.exampleservice2.server.activity.ping;

import com.google.common.base.Throwables;
import nl.arthurvlug.captainhook.exampleservice2.activity.ping.PingInput;
import nl.arthurvlug.captainhook.exampleservice2.activity.ping.PingOutput;
import nl.arthurvlug.captainhook.exampleservice2.server.activity.AbstractExampleActivity;
import nl.arthurvlug.captainhook.framework.server.Activity;
import org.springframework.stereotype.Component;
import rx.Observable;

@Activity
@Component
public class PingActivity extends AbstractExampleActivity<PingInput, PingOutput> {
    @Override
    public Observable<PingOutput> enact(PingInput helloWorldInput) {
        final long sleepTime = (long) (Math.random() * 500);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw Throwables.propagate(e);
        }
        if(Math.random() < 0.1) {
            throw new RuntimeException("Purposely thrown an exception after " + sleepTime + "ms");
        }
        final PingOutput output = PingOutput.builder()
                .y("Pong!")
                .build();
        return Observable.just(output);
    }
}