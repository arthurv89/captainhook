package nl.arthurvlug.captainhook.exampleservice.activity.helloworld;

import nl.arthurvlug.captainhook.exampleservice.server.activity.AbstractExampleActivity;
import nl.arthurvlug.captainhook.framework.server.Activity;

@Activity
public class HelloWorldActivity extends AbstractExampleActivity<HelloWorldInput, HelloWorldOutput> {
    @Override
    public HelloWorldOutput enact(HelloWorldInput helloWorldInput) throws InterruptedException {
        if(Math.random() < 0.3) {
            throw new RuntimeException("Wrong argument");
        }
        Thread.sleep((long) (Math.random() * 1000));
        return new HelloWorldOutput("Hello, " + helloWorldInput.getName() + "!");
    }
}