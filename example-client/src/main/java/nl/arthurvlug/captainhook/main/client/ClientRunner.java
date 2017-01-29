package nl.arthurvlug.captainhook.main.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldInput;
import nl.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldOutput;
import nl.arthurvlug.captainhook.exampleservice.client.ExampleServiceClient;
import nl.arthurvlug.captainhook.framework.client.AbstractClientRunner;
import nl.arthurvlug.captainhook.framework.common.response.DependencyException;
import org.apache.http.conn.HttpHostConnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientRunner extends AbstractClientRunner {
    @Autowired
    private ExampleServiceClient exampleService;

    @Override
    public void run() {
        while(true) {
            doHelloWorldCall();
        }
    }

    private void doHelloWorldCall() {
        try {
            final HelloWorldInput helloWorldInput = HelloWorldInput.builder()
                    .name("World")
                    .build();
            final HelloWorldOutput output = exampleService.helloWorldCall(helloWorldInput).call();
            log.info("Responded at timestamp {}: {}",
                    output.getRespondingTime().getTime().getTime(),
                    output.getMessage());
        } catch (DependencyException e) {
            if(e.getCause() instanceof HttpHostConnectException) {
                log.error("Server not ready. Waiting 500 ms");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e2) {
                    throw new RuntimeException(e2);
                }
            } else {
                log.error(e.getCause().getClass().getSimpleName() + ": " + e.getCause().getMessage());
            }
        }
    }
}
