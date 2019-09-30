package com.arthurvlug.captainhook.exampleservice2;

import com.arthurvlug.captainhook.exampleservice2.server.ServerProperties;
import com.arthurvlug.captainhook.framework.server.Controller;

public class ServiceMain2 {
    public static void main(String[] args) {
        Controller.run(new ServerProperties(), args);
    }
}
