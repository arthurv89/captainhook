package com.arthurvlug.captainhook.examplemiddleservice;

import com.arthurvlug.captainhook.examplemiddleservice.server.ServerSpringComponentsImporter;
import nl.arthurvlug.captainhook.framework.server.Controller;

public class MiddleServiceMain {
    public static void main(String[] args) {
        Controller.run(ServerSpringComponentsImporter.class, args);
    }
}
