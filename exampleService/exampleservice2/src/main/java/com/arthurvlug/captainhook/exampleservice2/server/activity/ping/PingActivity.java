package com.arthurvlug.captainhook.exampleservice2.server.activity.ping;

import com.arthurvlug.captainhook.exampleservice2.activity.ping.PingInput;
import com.arthurvlug.captainhook.exampleservice2.activity.ping.PingOutput;
import com.arthurvlug.captainhook.framework.server.SimpleActivity;
import com.arthurvlug.captainhook.framework.server.Activity;
import org.springframework.stereotype.Component;
import rx.Observable;

@Activity
@Component
public class PingActivity extends SimpleActivity<PingInput, PingOutput> {
    @Override
    public Observable<PingOutput> enact(PingInput helloWorldInput) {
        final PingOutput output = PingOutput.builder()
                .y("Pong!")
                .build();
        return Observable.just(output);
    }
}
