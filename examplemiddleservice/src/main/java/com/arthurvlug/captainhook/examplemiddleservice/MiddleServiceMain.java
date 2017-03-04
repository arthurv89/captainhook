package com.arthurvlug.captainhook.examplemiddleservice;

import com.arthurvlug.captainhook.examplemiddleservice.server.ServerSpringComponentsImporter;
import nl.arthurvlug.captainhook.framework.server.spring.CaptainHookSpringController;

public class MiddleServiceMain {
    public static void main(String[] args) {
        CaptainHookSpringController.run(ServerSpringComponentsImporter.class, args);
    }
}
