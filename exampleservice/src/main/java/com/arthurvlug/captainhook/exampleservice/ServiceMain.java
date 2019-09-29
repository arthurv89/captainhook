package com.arthurvlug.captainhook.exampleservice;

import com.arthurvlug.captainhook.exampleservice.server.ServerProperties;
import com.arthurvlug.captainhook.framework.server.Controller;
import com.arthurvlug.captainhook.framework.server.ServerEndpointComponent;

public class ServiceMain {
    public static void main(String[] args) {
        new Controller(new ServerProperties()).run(args);
    }
}
