package com.arthurvlug.captainhook.exampleservice2;

import com.arthurvlug.captainhook.exampleservice2.server.SelfDiagnose;
import com.arthurvlug.captainhook.exampleservice2.server.ServerProperties;
import com.arthurvlug.captainhook.framework.server.Controller;
import com.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.SelfDiagnoseController;
import com.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.SelfDiagnosePlugin;

public class ServiceMain2 {
    public static void main(String[] args) {
        Controller.run(
                new ServerProperties(),
                new SelfDiagnosePlugin(SelfDiagnose.class),
                args);
    }
}
