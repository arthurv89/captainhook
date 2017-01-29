package nl.arthurvlug.captainhook.exampleservice.activity.helloworld;

import nl.arthurvlug.captainhook.exampleservice.server.activity.AbstractExampleActivity;
import nl.arthurvlug.captainhook.framework.server.Activity;

@Activity
public class HelloWorldActivity extends AbstractExampleActivity<HelloWorldInput, HelloWorldOutput> {
    @Override
    public HelloWorldOutput enact(HelloWorldInput helloWorldInput) throws InterruptedException {
        Thread.sleep((long) (Math.random() * 2000));
        if(Math.random() < 0.3) {
            throw new RuntimeException("Purposely thrown an exception");
        }
        return new HelloWorldOutput("Hello, " + helloWorldInput.getName() + "!");
    }
}