package com.arthurvlug.captainhook.exampleservice.server.activity.helloworld;

import com.google.common.base.Throwables;
import com.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldInput;
import com.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldOutput;
import com.arthurvlug.captainhook.exampleservice.server.activity.AbstractExampleActivity;
import com.arthurvlug.captainhook.framework.server.Activity;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.time.Instant;

@Activity
@Component
public class HelloWorldActivity extends AbstractExampleActivity<HelloWorldInput, HelloWorldOutput> {
    @Override
    public Observable<HelloWorldOutput> enact(HelloWorldInput helloWorldInput) {
        final long sleepTime = (long) (Math.random() * 5000);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw Throwables.propagate(e);
        }
        if(Math.random() < 0.1) {
            throw new RuntimeException(String.format("Purposely thrown an exception after %dms", sleepTime));
        }
        final HelloWorldOutput output = HelloWorldOutput.builder()
                .message(String.format("[HelloWorld (slept: %dms): Hello, %s!]", sleepTime, helloWorldInput.getName()))
                .respondingTime(Instant.now())
                .build();
        return Observable.just(output);
    }
}