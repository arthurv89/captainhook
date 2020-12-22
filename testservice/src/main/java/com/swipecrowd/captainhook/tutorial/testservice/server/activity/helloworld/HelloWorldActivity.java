package com.swipecrowd.captainhook.tutorial.testservice.server.activity.helloworld;

import com.swipecrowd.captainhook.framework.server.Activity;
import com.swipecrowd.captainhook.framework.server.ActivityRequest;
import com.swipecrowd.captainhook.framework.server.SimpleActivity;
import com.swipecrowd.captainhook.tutorial.testservice.activity.helloworld.HelloWorldInput;
import com.swipecrowd.captainhook.tutorial.testservice.activity.helloworld.HelloWorldOutput;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.time.Instant;

@Activity
@Component
public class HelloWorldActivity extends SimpleActivity<HelloWorldInput, HelloWorldOutput> {
    @Override
    public Observable<HelloWorldOutput> handle(ActivityRequest<HelloWorldInput> helloWorldInput) {
        final HelloWorldOutput output = HelloWorldOutput.builder()
                .message(String.format("HelloMoonService received HelloWorldInput with name property: \"%s\"", helloWorldInput.getInput().getName()))
                .respondingTime(Instant.now())
                .build();

        return Observable.just(output);
    }
}

