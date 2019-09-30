package com.arthurvlug.captainhook.exampleservice.server;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import com.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.AbstractSelfDiagnose;
import com.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.SelfDiagnoseItem;
import com.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.SelfDiagnoseLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SelfDiagnose extends AbstractSelfDiagnose {
    private ServerProperties serverProperties = new ServerProperties();

    @Getter
    private final SelfDiagnoseLayout layout = new SelfDiagnoseLayout("#dddddd");

    public String getName() {
        return serverProperties.getServerName();
    }

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
