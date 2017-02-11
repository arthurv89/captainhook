package nl.arthurvlug.captainhook.exampleservice.server.activity.helloworld;

import com.google.common.base.Throwables;
import nl.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldInput;
import nl.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldOutput;
import nl.arthurvlug.captainhook.exampleservice.server.activity.AbstractExampleActivity;
import nl.arthurvlug.captainhook.framework.server.Activity;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.util.Calendar;

@Activity
@Component
public class HelloWorldActivity extends AbstractExampleActivity<HelloWorldInput, HelloWorldOutput> {
    @Override
    public Observable<HelloWorldOutput> enact(HelloWorldInput helloWorldInput) {
        final long sleepTime = (long) (Math.random() * 5000);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw Throwables.propagate(e);
        }
        if(Math.random() < 0.1) {
            throw new RuntimeException("Purposely thrown an exception after " + sleepTime + "ms");
        }
        final HelloWorldOutput output = HelloWorldOutput.builder()
                .message("[HelloWorld (slept: " + sleepTime + "ms): Hello, " + helloWorldInput.getName() + "!]")
                .respondingTime(Calendar.getInstance())
                .build();
        return Observable.just(output);
    }
}