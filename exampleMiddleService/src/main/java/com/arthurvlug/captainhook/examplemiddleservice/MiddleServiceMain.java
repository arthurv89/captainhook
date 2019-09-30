package com.arthurvlug.captainhook.examplemiddleservice;

import com.arthurvlug.captainhook.examplemiddleservice.server.ServerProperties;
import com.arthurvlug.captainhook.framework.server.Controller;

public class MiddleServiceMain {
    public static void main(String[] args) {
        Controller.run(new ServerProperties(), args);
    }
}
