package com.swipecrowd.captainhook.test.testservice.server.activity.helloworld;

import com.google.gson.Gson;
import com.swipecrowd.captainhook.framework.server.AbstractServerProperties;
import com.swipecrowd.captainhook.test.testservice.TestServiceServerProperties;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldInput;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldOutput;
import com.swipecrowd.captainhook.test.testservice.client.TestServiceJavaClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.time.Instant;

@Service
@AllArgsConstructor
public class HelloWorldService {
    @Autowired
    private final TestServiceJavaClient testServiceJavaClient;

    @Autowired
    private final TestServiceServerProperties testServiceServerProperties;


    Observable<HelloWorldOutput> createNormalOutput(final HelloWorldInput helloWorldInput) {
        return Observable.just(createOutput(testServiceServerProperties.getPort() + " -> Received name: " + helloWorldInput.getName()));
    }

    Observable<HelloWorldOutput> handleShowConfig() {
        return Observable.just(HelloWorldOutput.builder().message(toJson(testServiceJavaClient.serverProperties)).build());
    }

    private String toJson(final AbstractServerProperties serverProperties) {
        return new Gson().toJson(serverProperties);
    }

    Observable<HelloWorldOutput> handleForwardCommand(final HelloWorldInput helloWorldInput) {
        final HelloWorldInput newHelloWorldInput = HelloWorldInput.builder()
                .name(helloWorldInput.getName())
                .forward(helloWorldInput.getForward()-1)
                .build();

        return testServiceJavaClient.helloWorldCall(newHelloWorldInput)
                .map(response -> {
                    return createOutput(response.getMessage());
                });
    }

    Observable<HelloWorldOutput> handleDestroyCommand() {
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

    private HelloWorldOutput createOutput(final String message) {
        return HelloWorldOutput.builder()
                .message(message)
                .respondingTime(Instant.now())
                .build();
    }
}
