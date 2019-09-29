package com.arthurvlug.captainhook.examplemiddleservice.server;

import com.arthurvlug.captainhook.examplemiddleservice.common.ServiceConfiguration;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.AbstractSelfDiagnose;
import nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.SelfDiagnoseItem;
import nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.SelfDiagnoseLayout;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SelfDiagnose extends AbstractSelfDiagnose {
    @Getter
    private final SelfDiagnoseLayout layout = new SelfDiagnoseLayout("#dddddd");

    @Getter
    private final String name = ServiceConfiguration.name;

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
