package nl.arthurvlug.captainhook.exampleservice2.server.activity.ping;

import nl.arthurvlug.captainhook.exampleservice2.activity.ping.PingInput;
import nl.arthurvlug.captainhook.exampleservice2.activity.ping.PingOutput;
import nl.arthurvlug.captainhook.exampleservice2.server.activity.AbstractExampleActivity;
import nl.arthurvlug.captainhook.framework.server.Activity;

@Activity
public class PingActivity extends AbstractExampleActivity<PingInput, PingOutput> {
    @Override
    public PingOutput enact(PingInput helloWorldInput) throws InterruptedException {
        Thread.sleep((long) (Math.random() * 2000));
        if(Math.random() < 0.3) {
            throw new RuntimeException("Purposely thrown an exception");
        }
        return PingOutput.builder()
                .y("Pong!")
                .build();
    }
}