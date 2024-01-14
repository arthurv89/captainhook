package com.swipecrowd.captainhook.test.testservice.server.activity.helloworld;

import com.swipecrowd.captainhook.framework.application.server.resilience.ServiceCall;
import com.swipecrowd.captainhook.test.testservice.HelloWorldCache;
import com.swipecrowd.captainhook.test.testservice.TestServiceServerProperties;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldInput;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldOutput;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class HelloWorldService {
//    @Autowired private final TestServiceJavaClient testServiceJavaClient;
    @Autowired private final TestServiceServerProperties testServiceServerProperties;
    @Autowired private final ServiceCall serviceCall;
    @Autowired private final HelloWorldCache cache;

    private static HelloWorldOutput getRecoveryOutput(final Throwable e) {
        return HelloWorldOutput.builder().message("Recovered from a failure: " + e.getMessage()).build();
    }

    HelloWorldOutput createNormalOutput(final HelloWorldInput helloWorldInput) {
        return createOutput(testServiceServerProperties.getPort() + " -> Received name: " + helloWorldInput.getName());
    }

    HelloWorldOutput handleForwardCommand(final HelloWorldInput _helloWorldInput) {
        final HelloWorldInput newHelloWorldInput = HelloWorldInput.builder()
                .name(_helloWorldInput.getName())
                .forward(_helloWorldInput.getForward()-1)
                .build();

//        return serviceCall.run(testServiceJavaClient.helloWorldCall(newHelloWorldInput))
//                .map(x -> {
//                    cache.put(newHelloWorldInput, createOutput("CACHED"));
//                    return x;
//                })
//                .onErrorReturn(e -> {
//                    return cache.get(newHelloWorldInput).map(x -> createOutput("CACHED " + e.getMessage())).orElse(getRecoveryOutput(e));
//                })
//                .blockingFirst();
        return null;
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
