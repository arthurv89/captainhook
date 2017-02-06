package nl.arthurvlug.captainhook.exampleservice.server;

import com.google.common.collect.ImmutableList;
import nl.arthurvlug.captainhook.exampleservice.ServiceConfiguration;
import nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.AbstractSelfDiagnose;
import nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.SelfDiagnoseItem;
import nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.SelfDiagnoseLayout;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SelfDiagnose extends AbstractSelfDiagnose {
    @Override
    public SelfDiagnoseLayout getLayout() {
        return new SelfDiagnoseLayout("#AF4C50");
    }

    @Override
    public String getName() {
        return ServiceConfiguration.name;
    }

    @Override
    public List<SelfDiagnoseItem> getItems() {
        return ImmutableList.of(
                new SelfDiagnoseItem("Status", "Everything OK"),
                new SelfDiagnoseItem("Sky", "dark"),
                new SelfDiagnoseItem("Random Value", Double.toString(Math.random()))
        );
    }

    @Override
    public String getVersion() {
        return "8.99";
    }
}
