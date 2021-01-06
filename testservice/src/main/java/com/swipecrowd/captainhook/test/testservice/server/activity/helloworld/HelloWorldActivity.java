package com.swipecrowd.captainhook.test.testservice.server.activity.helloworld;

import com.swipecrowd.captainhook.framework.application.server.SimpleActivity;
import com.swipecrowd.captainhook.test.testservice.TestServiceServerProperties;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldInput;
import com.swipecrowd.captainhook.test.testservice.activity.helloworld.HelloWorldOutput;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.swipecrowd.captainhook.test.testservice.TestServiceServerProperties.DESTROY_KEY;

@AllArgsConstructor
@RestController
public class HelloWorldActivity extends SimpleActivity<HelloWorldInput, HelloWorldOutput> {
    @Autowired private final HelloWorldService helloWorldService;
    @Autowired private final TestServiceServerProperties testServiceServerProperties;

    @RequestMapping(path = "/helloworld", method = RequestMethod.POST)
    @Override
    public HelloWorldOutput handle(@RequestBody final HelloWorldInput helloWorldInput) {
        System.out.printf("[%d] %s%n", testServiceServerProperties.getPort(), helloWorldInput);

        if(helloWorldInput.getName().equals(DESTROY_KEY)) {
            return helloWorldService.handleDestroyCommand();
        }

        if(helloWorldInput.getForward() == 0) {
            return helloWorldService.createNormalOutput(helloWorldInput);
        }

        return helloWorldService.handleForwardCommand(helloWorldInput);
    }
}
