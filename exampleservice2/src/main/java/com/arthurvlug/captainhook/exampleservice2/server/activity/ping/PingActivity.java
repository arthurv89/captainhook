package com.arthurvlug.captainhook.exampleservice2.server.activity.ping;

import com.google.common.base.Throwables;
import com.arthurvlug.captainhook.exampleservice2.activity.ping.PingInput;
import com.arthurvlug.captainhook.exampleservice2.activity.ping.PingOutput;
import com.arthurvlug.captainhook.exampleservice2.server.activity.AbstractExampleActivity;
import com.arthurvlug.captainhook.framework.server.Activity;
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
            throw new RuntimeException(String.format("Purposely thrown an exception after %dms", sleepTime));
        }
        final PingOutput output = PingOutput.builder()
                .y("Pong!")
                .build();
        return Observable.just(output);
    }
}