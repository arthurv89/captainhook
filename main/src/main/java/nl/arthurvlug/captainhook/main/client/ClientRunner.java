package nl.arthurvlug.captainhook.main.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldInput;
import nl.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldOutput;
import nl.arthurvlug.captainhook.exampleservice2.activity.ping.PingInput;
import nl.arthurvlug.captainhook.exampleservice2.activity.ping.PingOutput;
import nl.arthurvlug.captainhook.framework.client.AbstractClientRunner;
import nl.arthurvlug.captainhook.framework.client.AbstractService;
import nl.arthurvlug.captainhook.framework.common.response.DependencyException;
import org.apache.http.conn.HttpHostConnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientRunner extends AbstractClientRunner {
    @Autowired
    private nl.arthurvlug.captainhook.exampleservice.client.Client exampleService;

    @Autowired
    private nl.arthurvlug.captainhook.exampleservice2.client.Client exampleService2;

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
            log.info("Service 1: Responded at timestamp {}: {}",
                    output.getRespondingTime().getTime().getTime(),
                    output.getMessage());
        } catch (DependencyException e) {
            handleError(e, exampleService);
        }


        try {
            final nl.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldInput helloWorldInput = nl.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldInput.builder()
                    .name("World")
                    .build();
            final nl.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldOutput output = exampleService2.helloWorldCall(helloWorldInput).call();
            log.info("Service 2A: Responded at timestamp {}: {}",
                    output.getRespondingTime().getTime().getTime(),
                    output.getMessage());
        } catch (DependencyException e) {
            handleError(e, exampleService2);
        }

        try {
            final PingInput pingInput = PingInput.builder()
                    .x("World")
                    .build();
            final PingOutput output = exampleService2.pingCall(pingInput).call();
            log.info("Service 2B: Responded: {}",
                    output.getY());
        } catch (Exception e) {
            handleError(e, exampleService);
        }

        System.out.println("");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e2) {
            throw new RuntimeException(e2);
        }
    }

    private void handleError(final Exception e, AbstractService clientName) {
        if (e.getCause() instanceof HttpHostConnectException) {
            log.error("Server " + clientName.getClass().getName() + " not ready. Waiting 500 ms");
        } else {
            log.error(e.getCause().getClass().getName() + ": " + e.getCause().getMessage());
        }
    }
}
