package nl.arthurvlug.captainhook.main.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldInput;
import nl.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldOutput;
import nl.arthurvlug.captainhook.exampleservice.client.ExampleServiceClient;
import nl.arthurvlug.captainhook.framework.client.AbstractClientRunner;
import nl.arthurvlug.captainhook.framework.common.response.DependencyException;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientRunner extends AbstractClientRunner {
    private static final Logger logger = LoggerFactory.getLogger(ClientRunner.class);

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
                    .name("World (" + new Date().getTime() + ")")
                    .build();
            final HelloWorldOutput output = exampleService.helloWorldCall(helloWorldInput).call();
            logger.info(output.getMessage());
        } catch (DependencyException e) {
            if(e.getCause() instanceof HttpHostConnectException) {
                logger.error("Server not ready. Waiting 500 ms");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e2) {
                    throw new RuntimeException(e2);
                }
            } else {
                logger.error(e.getCause().getClass().getSimpleName() + ": " + e.getCause().getMessage());
            }
        }
    }
}
