package com.arthurvlug.captainhook.tutorial.hellomoonservice;

import com.arthurvlug.captainhook.tutorial.hellomoonservice.server.ServerProperties;
import com.arthurvlug.captainhook.framework.server.Controller;

public class ServiceMain {
    public static void main(String[] args) {
        Controller.run(
                new ServerProperties(),
                args);
    }
}
