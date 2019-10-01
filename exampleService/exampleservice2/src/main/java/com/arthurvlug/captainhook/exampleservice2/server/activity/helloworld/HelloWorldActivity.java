package com.arthurvlug.captainhook.exampleservice2.server.activity.helloworld;

import com.google.common.base.Throwables;
import com.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldInput;
import com.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldOutput;
import com.arthurvlug.captainhook.framework.server.SimpleActivity;
import com.arthurvlug.captainhook.framework.server.Activity;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.time.Instant;

@Activity
@Component
public class HelloWorldActivity extends SimpleActivity<HelloWorldInput, HelloWorldOutput> {
    @Override
    public Observable<HelloWorldOutput> enact(HelloWorldInput helloWorldInput) {
        final long sleepTime = (long) (Math.random() * 4000);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw Throwables.propagate(e);
        }
        if(Math.random() < 0.1) {
            throw new RuntimeException(String.format("Purposely thrown an exception after %dms", sleepTime));
        }
        final HelloWorldOutput output = HelloWorldOutput.builder()
                .message(String.format("[HelloWorld 2 (slept: %dms): Hello, %s!]", sleepTime, helloWorldInput.getName()))
                .respondingTime(Instant.now())
                .build();
        return Observable.just(output);
    }
}