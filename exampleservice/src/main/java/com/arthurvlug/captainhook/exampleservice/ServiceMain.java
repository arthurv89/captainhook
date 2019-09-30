package com.arthurvlug.captainhook.exampleservice;

import com.arthurvlug.captainhook.exampleservice.server.ServerProperties;
import com.arthurvlug.captainhook.framework.server.Controller;

public class ServiceMain {
    public static void main(String[] args) {
        Controller.run(new ServerProperties(), args);
    }
}
