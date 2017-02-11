package com.arthurvlug.captainhook.examplemiddleservice.server.activity.merge;

import com.arthurvlug.captainhook.examplemiddleservice.activity.merge.MergeInput;
import com.arthurvlug.captainhook.examplemiddleservice.activity.merge.MergeOutput;
import com.arthurvlug.captainhook.examplemiddleservice.server.activity.AbstractExampleActivity;
import lombok.AllArgsConstructor;
import lombok.Value;
import nl.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldInput;
import nl.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldOutput;
import nl.arthurvlug.captainhook.framework.server.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.util.Calendar;

@Activity
@Component
public class MergeActivity extends AbstractExampleActivity<MergeInput, MergeOutput> {
    @Autowired
    @Qualifier("exampleserviceClient")
    private nl.arthurvlug.captainhook.exampleservice.client.Client exampleServiceClient;

    @Autowired
    @Qualifier("exampleservice2Client")
    private nl.arthurvlug.captainhook.exampleservice2.client.Client exampleService2Client;

    @Override
    public Observable<MergeOutput> enact(MergeInput mergeInput) {
        final HelloWorldInput input1 = HelloWorldInput.builder()
                .name(mergeInput.getName() + " [1]")
                .build();
        final nl.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldInput input2 = nl.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldInput.builder()
                .name(mergeInput.getName() + " [2]")
                .build();

        final Observable<HelloWorldOutput> call1 = exampleServiceClient.helloWorldCall(input1);
        final Observable<nl.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldOutput> call2 = exampleService2Client.helloWorldCall(input2);

        return combine(call1, call2)
                .map(combined -> MergeOutput.builder()
                        .message("[Combined: " + combined.getHelloWorldOutput().getMessage() + ", " + combined.getHelloWorld2Output().getMessage() + "]")
                        .respondingTime(Calendar.getInstance())
                        .build());
    }

    @Value
    @AllArgsConstructor
    class CombinedValue {
        HelloWorldOutput helloWorldOutput;
        nl.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldOutput helloWorld2Output;
    }

    private Observable<CombinedValue> combine(final Observable<HelloWorldOutput> helloworld,
                                              final Observable<nl.arthurvlug.captainhook.exampleservice2.activity.helloworld.HelloWorldOutput> helloWorld2) {
        return helloworld.flatMap(helloWorldOutput -> helloWorld2.map(helloWorld2Output -> new CombinedValue(helloWorldOutput, helloWorld2Output)));
    }
}