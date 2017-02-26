package nl.arthurvlug.captainhook.exampleservice.server;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import nl.arthurvlug.captainhook.framework.server.AbstractServiceConfiguration;
import nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.AbstractSelfDiagnose;
import nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.SelfDiagnoseItem;
import nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.SelfDiagnoseLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SelfDiagnose extends AbstractSelfDiagnose {
    @Autowired
    private AbstractServiceConfiguration serviceConfiguration;

    @Getter
    private final SelfDiagnoseLayout layout = new SelfDiagnoseLayout("#dddddd");

    @Getter
    private final String name = serviceConfiguration.getName();

    @Getter
    private List<SelfDiagnoseItem> items = new ArrayList<>();

    @Getter
    private final String version = "8.99";


    @Override
    protected void refresh() {
        items = ImmutableList.of(
                new SelfDiagnoseItem("Status", "Everything OK"),
                new SelfDiagnoseItem("Sky", "dark")
        );
    }
}
