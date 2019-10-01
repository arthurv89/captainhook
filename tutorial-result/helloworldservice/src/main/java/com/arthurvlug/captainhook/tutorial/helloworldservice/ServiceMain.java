package com.arthurvlug.captainhook.tutorial.helloworldservice;

import com.arthurvlug.captainhook.tutorial.helloworldservice.server.ServerProperties;
import com.arthurvlug.captainhook.framework.server.Controller;

public class ServiceMain {
    public static void main(String[] args) {
        Controller.run(
                new ServerProperties(),
                args);
    }
}
