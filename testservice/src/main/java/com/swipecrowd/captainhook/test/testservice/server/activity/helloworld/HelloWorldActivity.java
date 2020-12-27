package com.swipecrowd.captainhook.test.testservice.server.activity.helloworld;

import com.swipecrowd.captainhook.framework.server.Activity;
import com.swipecrowd.captainhook.framework.server.ActivityRequest;
import com.swipecrowd.captainhook.framework.server.SimpleActivity;
import com.swipecrowd.captainhook.test.testservice.TestServiceServerProperties;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldInput;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldOutput;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import static com.swipecrowd.captainhook.test.testservice.TestServiceServerProperties.DESTROY_KEY;
import static com.swipecrowd.captainhook.test.testservice.TestServiceServerProperties.SHOW_CONFIG_KEY;

@Activity
@Component
@AllArgsConstructor
public class HelloWorldActivity extends SimpleActivity<HelloWorldInput, HelloWorldOutput> {
    @Autowired private final HelloWorldService helloWorldService;
    @Autowired private final TestServiceServerProperties testServiceServerProperties;

    @Override
    public Observable<HelloWorldOutput> handle(ActivityRequest<HelloWorldInput> activityRequest) {
        final HelloWorldInput helloWorldInput = activityRequest.getInput();
        System.out.printf("[%d] %s%n", testServiceServerProperties.getPort(), helloWorldInput);

        if(helloWorldInput.getName().equals(DESTROY_KEY)) {
            return helloWorldService.handleDestroyCommand();
        }

        if(helloWorldInput.getName().equals(SHOW_CONFIG_KEY)) {
            return helloWorldService.handleShowConfig();
        }

        if(helloWorldInput.getForward() == 0) {
            return helloWorldService.createNormalOutput(helloWorldInput);
        }

        return helloWorldService.handleForwardCommand(helloWorldInput);
    }
}

