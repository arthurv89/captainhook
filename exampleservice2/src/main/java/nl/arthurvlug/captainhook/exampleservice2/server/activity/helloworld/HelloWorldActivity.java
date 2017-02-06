package nl.arthurvlug.captainhook.exampleservice2.server.activity.helloworld;

import nl.arthurvlug.captainhook.exampleservice2.server.activity.AbstractExampleActivity;
import nl.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldInput;
import nl.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldOutput;
import nl.arthurvlug.captainhook.framework.server.Activity;

import java.util.Calendar;

@Activity
public class HelloWorldActivity extends AbstractExampleActivity<HelloWorldInput, HelloWorldOutput> {
    @Override
    public HelloWorldOutput enact(HelloWorldInput helloWorldInput) throws InterruptedException {
        Thread.sleep((long) (Math.random() * 500));
        if(Math.random() < 0.1) {
            throw new RuntimeException("Purposely thrown an exception");
        }
        return HelloWorldOutput.builder()
                .message("Hello, " + helloWorldInput.getName() + "!")
                .respondingTime(Calendar.getInstance())
                .build();
    }
}