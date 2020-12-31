package com.swipecrowd.captainhook.test.testservice.server.activity.helloworld;

import com.swipecrowd.captainhook.test.testservice.TestServiceServerProperties;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldInput;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldOutput;
import com.swipecrowd.captainhook.test.testservice.client.TestServiceJavaClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class HelloWorldService {
    @Autowired
    private final TestServiceJavaClient testServiceJavaClient;

    @Autowired
    private final TestServiceServerProperties testServiceServerProperties;


    HelloWorldOutput createNormalOutput(final HelloWorldInput helloWorldInput) {
        return createOutput(testServiceServerProperties.getPort() + " -> Received name: " + helloWorldInput.getName());
    }

    HelloWorldOutput handleForwardCommand(final HelloWorldInput helloWorldInput) {
        final HelloWorldInput newHelloWorldInput = HelloWorldInput.builder()
                .name(helloWorldInput.getName())
                .forward(helloWorldInput.getForward()-1)
                .build();

        return testServiceJavaClient.helloWorldCall(newHelloWorldInput)
                .map(response -> {
                    return createOutput(response.getMessage());
                })
                .toBlocking()
                .first();
    }

    HelloWorldOutput handleDestroyCommand() {
        new Thread(() -> {
            try {
                System.out.println("Self destructing in 3000 ms");
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            }
            System.exit(1);
        }).start();
        return createDestroyOutput();
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
