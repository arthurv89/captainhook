package nl.arthurvlug.captainhook.exampleservice2.server;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import nl.arthurvlug.captainhook.exampleservice2.common.ServiceConfiguration;
import nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.AbstractSelfDiagnose;
import nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.SelfDiagnoseItem;
import nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.SelfDiagnoseItemValue;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SelfDiagnose extends AbstractSelfDiagnose {
    @Getter
    private final String name = ServiceConfiguration.name;

    @Getter
    private List<SelfDiagnoseItem> items = new ArrayList<>();

    @Getter
    private final String version = "5.6.7";

    @Override
    protected void refresh() {
        final double random = Math.random();
        final String sRandom = String.valueOf(random);
        items = ImmutableList.of(
                new SelfDiagnoseItem("Random", new SelfDiagnoseItemValue(randomMoreThanHalf(random), sRandom)),
                new SelfDiagnoseItem("Sky", "blue")
        );
    }

    private boolean randomMoreThanHalf(final double random) {
        return random > 0.5;
    }
}
