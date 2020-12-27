package com.swipecrowd.captainhook.test.testservice.server.activity.helloworld;

import com.google.gson.Gson;
import com.swipecrowd.captainhook.framework.server.AbstractServerProperties;
import com.swipecrowd.captainhook.framework.server.Activity;
import com.swipecrowd.captainhook.framework.server.ActivityRequest;
import com.swipecrowd.captainhook.framework.server.SimpleActivity;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldInput;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldOutput;
import com.swipecrowd.captainhook.test.testservice.client.TestServiceJavaClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.time.Instant;

import static com.swipecrowd.captainhook.test.testservice.TestServiceServerProperties.DESTROY_KEY;
import static com.swipecrowd.captainhook.test.testservice.TestServiceServerProperties.SHOW_CONFIG_KEY;

@Activity
@Component
@AllArgsConstructor
public class HelloWorldActivity extends SimpleActivity<HelloWorldInput, HelloWorldOutput> {
    @Autowired private final TestServiceJavaClient testServiceJavaClient;

    @Override
    public Observable<HelloWorldOutput> handle(ActivityRequest<HelloWorldInput> activityRequest) {
        final HelloWorldInput helloWorldInput = activityRequest.getInput();
        System.out.println(helloWorldInput);

        if(helloWorldInput.getName().equals(DESTROY_KEY)) {
            return handleDestroyCommand();
        }

        if(helloWorldInput.getName().equals(SHOW_CONFIG_KEY)) {
            return handleShowConfig();
        }

        if(helloWorldInput.getForward() == 0) {
            return Observable.just(createOutput(helloWorldInput));
        }

        return handleForwardCommand(helloWorldInput);
    }

    private Observable<HelloWorldOutput> handleShowConfig() {
        return Observable.just(HelloWorldOutput.builder().message(toJson(testServiceJavaClient.serverProperties)).build());
    }

    private String toJson(final AbstractServerProperties serverProperties) {
        return new Gson().toJson(serverProperties);
    }

    private Observable<HelloWorldOutput> handleForwardCommand(final HelloWorldInput helloWorldInput) {
        final HelloWorldInput newHelloWorldInput = HelloWorldInput.builder()
                .name(helloWorldInput.getName())
                .forward(helloWorldInput.getForward()-1)
                .build();

        return testServiceJavaClient.helloWorldCall(newHelloWorldInput)
                .map(response -> createOutput(newHelloWorldInput));
    }

    private Observable<HelloWorldOutput> handleDestroyCommand() {
        new Thread(() -> {
            try {
                System.out.println("Self destructing in 3000 ms");
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            }
            System.exit(1);
        }).start();
        return Observable.just(createDestroyOutput());
    }

    private HelloWorldOutput createDestroyOutput() {
        return HelloWorldOutput.builder().message("SELF DESTRUCTING...").build();
    }

    private HelloWorldOutput createOutput(final HelloWorldInput helloWorldInput) {
        return HelloWorldOutput.builder()
                .message(testServiceJavaClient.serverProperties.getPort() + " -> Received name: " + helloWorldInput.getName())
                .respondingTime(Instant.now())
                .build();
    }
}

