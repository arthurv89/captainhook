package com.swipecrowd.captainhook.test.testservice.server.activity.helloworld;

import com.swipecrowd.captainhook.framework.server.AbstractServerProperties;
import com.swipecrowd.captainhook.framework.server.Activity;
import com.swipecrowd.captainhook.framework.server.ActivityRequest;
import com.swipecrowd.captainhook.framework.server.SimpleActivity;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldInput;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldOutput;
import com.swipecrowd.captainhook.test.testservice.client.JavaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.time.Instant;

@Activity
@Component
public class HelloWorldActivity extends SimpleActivity<HelloWorldInput, HelloWorldOutput> {
    private final JavaClient javaClient;

    @Autowired
    public HelloWorldActivity(final AbstractServerProperties serverProperties) {
        javaClient = new JavaClient(serverProperties);
    }

    @Override
    public Observable<HelloWorldOutput> handle(ActivityRequest<HelloWorldInput> activityRequest) {
        final HelloWorldInput helloWorldInput = activityRequest.getInput();
        if(helloWorldInput.getForward() == 0) {
            return Observable.just(createOutput(helloWorldInput));
        }

        final HelloWorldInput newHelloWorldInput = HelloWorldInput.builder()
                .name(helloWorldInput.getName())
                .forward(helloWorldInput.getForward()-1)
                .build();

        return javaClient.helloWorldCall(newHelloWorldInput)
                .map(response -> createOutput(newHelloWorldInput));
    }

    private HelloWorldOutput createOutput(final HelloWorldInput helloWorldInput) {
        return HelloWorldOutput.builder()
                .message("Received name: " + helloWorldInput.getName())
                .respondingTime(Instant.now())
                .build();
    }
}

