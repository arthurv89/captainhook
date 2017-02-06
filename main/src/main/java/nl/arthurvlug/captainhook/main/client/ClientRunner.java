package nl.arthurvlug.captainhook.main.client;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import nl.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldInput;
import nl.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldOutput;
import nl.arthurvlug.captainhook.exampleservice2.activity.ping.PingInput;
import nl.arthurvlug.captainhook.exampleservice2.activity.ping.PingOutput;
import nl.arthurvlug.captainhook.framework.client.AbstractClientRunner;
import nl.arthurvlug.captainhook.framework.common.response.DependencyException;
import org.apache.http.conn.HttpHostConnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.util.Optional;

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
            doCalls();
        }
    }

    private void doCalls() {
        log.info("");
        final Optional<CombinedValue> combinedValueOptional = combine()
                .map(x -> Optional.of(x))
                .doOnError(t -> handleError((DependencyException) t))
                .onErrorResumeNext(t -> Observable.empty())
                .toBlocking()
                .firstOrDefault(Optional.empty());

        combinedValueOptional.ifPresent(combinedValue -> {
            log.info("Service exampleservice:  Responded at timestamp {}: {}",
                    combinedValue.helloWorldOutput.getRespondingTime().getTime().getTime(),
                    combinedValue.helloWorldOutput.getMessage());

            log.info("Service exampleservice2: Responded at timestamp {}: {}",
                    combinedValue.helloWorld2Output.getRespondingTime().getTime().getTime(),
                    combinedValue.helloWorld2Output.getMessage());

            log.info("Service exampleservice2: Responded: {}",
                    combinedValue.pingOutput.getY());

            log.info("CombinedValue:           " + combinedValue);
        });

        sleep(1000);
    }

    private Observable<CombinedValue> combine() {
        return helloworld()
                .flatMap(helloWorldOutput -> helloworld2()
                        .flatMap(helloWorld2Output -> ping()
                                .map(pingOutput -> new CombinedValue(helloWorldOutput, helloWorld2Output, pingOutput))));
    }

    @Value
    @AllArgsConstructor
    class CombinedValue {
        HelloWorldOutput helloWorldOutput;
        nl.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldOutput helloWorld2Output;
        PingOutput pingOutput;
    }

    private void sleep(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e2) {
            throw new RuntimeException(e2);
        }
    }

    private Observable<HelloWorldOutput> helloworld() {
        log.info("- Running Helloworld");
        final HelloWorldInput helloWorldInput = HelloWorldInput.builder()
                .name("World")
                .build();
        return exampleService.helloWorldCall(helloWorldInput)
                .doOnNext(x -> log.info("- Received HelloWorld"));
    }

    private Observable<nl.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldOutput> helloworld2() {
        log.info("--  Running Helloworld2");
        final nl.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldInput helloWorldInput = nl.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldInput.builder()
                .name("World")
                .build();
        return exampleService2.helloWorldCall(helloWorldInput)
                .doOnNext(x -> log.info("--  Received HelloWorld2"));
    }

    private Observable<PingOutput> ping() {
        log.info("---   Running Ping");
        final PingInput pingInput = PingInput.builder()
                .x("World")
                .build();
        return exampleService2.pingCall(pingInput)
                .doOnNext(x -> log.info("---   Received Ping"));
    }

    private void handleError(final DependencyException e) {
        final String serverName = "Server " + e.getClientName();
        if (e.getCause() instanceof HttpHostConnectException) {
            log.error(serverName + " not ready. Waiting 500 ms");
        } else {
            log.error(serverName + ": " + e.getCause().getClass().getName() + ": " + e.getCause().getMessage());
        }
    }
}
