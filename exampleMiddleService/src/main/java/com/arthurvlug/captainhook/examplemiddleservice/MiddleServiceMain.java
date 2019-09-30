package com.arthurvlug.captainhook.examplemiddleservice;

import com.arthurvlug.captainhook.examplemiddleservice.server.SelfDiagnose;
import com.arthurvlug.captainhook.examplemiddleservice.server.ServerProperties;
import com.arthurvlug.captainhook.framework.server.Controller;
import com.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.SelfDiagnoseController;
import com.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.SelfDiagnosePlugin;

public class MiddleServiceMain {
    public static void main(String[] args) {
        Controller.run(
                new ServerProperties(),
                new SelfDiagnosePlugin(SelfDiagnose.class),
                args);
    }
}
