package com.arthurvlug.captainhook.examplemiddleservice.server.activity.merge;

import com.arthurvlug.captainhook.examplemiddleservice.activity.merge.MergeInput;
import com.arthurvlug.captainhook.examplemiddleservice.activity.merge.MergeOutput;
import com.arthurvlug.captainhook.examplemiddleservice.server.activity.AbstractExampleActivity;
import lombok.AllArgsConstructor;
import lombok.Value;
import com.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldInput;
import com.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldOutput;
import com.arthurvlug.captainhook.framework.server.Activity;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.time.Instant;

@Activity
@Component
public class MergeActivity extends AbstractExampleActivity<MergeInput, MergeOutput> {
    private com.arthurvlug.captainhook.exampleservice.client.Client exampleServiceClient = new com.arthurvlug.captainhook.exampleservice.client.Client();
    private com.arthurvlug.captainhook.exampleservice2.client.Client exampleService2Client = new com.arthurvlug.captainhook.exampleservice2.client.Client();

    @Override
    public Observable<MergeOutput> enact(MergeInput mergeInput) {
        final HelloWorldInput input1 = HelloWorldInput.builder()
                .name(mergeInput.getName())
                .build();
        final com.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldInput input2 = com.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldInput.builder()
                .name(mergeInput.getName())
                .build();

        final Observable<HelloWorldOutput> call1 = exampleServiceClient.helloWorldCall(input1);
        final Observable<com.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldOutput> call2 = exampleService2Client.helloWorldCall(input2);

        return combine(call1, call2)
                .map(combined -> MergeOutput.builder()
                        .message(String.format("[Combined: %s, %s]", combined.getHelloWorldOutput().getMessage(), combined.getHelloWorld2Output().getMessage()))
                        .respondingTime(Instant.now())
                        .build());
    }

    @Value
    @AllArgsConstructor
    class CombinedValue {
        HelloWorldOutput helloWorldOutput;
        com.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldOutput helloWorld2Output;
    }

    private Observable<CombinedValue> combine(final Observable<HelloWorldOutput> helloworld,
                                              final Observable<com.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldOutput> helloWorld2) {
        return helloworld.flatMap(helloWorldOutput -> helloWorld2.map(helloWorld2Output ->
                new CombinedValue(helloWorldOutput, helloWorld2Output)));
    }
}
