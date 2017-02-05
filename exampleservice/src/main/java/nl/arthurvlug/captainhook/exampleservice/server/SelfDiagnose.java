package nl.arthurvlug.captainhook.exampleservice.server;

import com.google.common.collect.ImmutableList;
import nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.AbstractSelfDiagnose;
import nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.SelfDiagnoseItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SelfDiagnose extends AbstractSelfDiagnose {
    @Override
    public List<SelfDiagnoseItem> getItems() {
        return ImmutableList.of(
                new SelfDiagnoseItem("Status", "green"),
                new SelfDiagnoseItem("Sky", "blue"),
                new SelfDiagnoseItem("Random Value", Double.toString(Math.random()))
        );
    }

    @Override
    public String getVersion() {
        return "5.6.7";
    }
}
