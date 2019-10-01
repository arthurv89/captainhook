package com.arthurvlug.captainhook.exampleservice;

import com.arthurvlug.captainhook.exampleservice.server.SelfDiagnose;
import com.arthurvlug.captainhook.exampleservice.server.ServerProperties;
import com.arthurvlug.captainhook.framework.server.Controller;
import com.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.SelfDiagnosePlugin;

public class ServiceMain {
    public static void main(String[] args) {
        Controller.run(
                new ServerProperties(),
                new SelfDiagnosePlugin(SelfDiagnose.class),
                args);
    }
}
