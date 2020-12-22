package com.swipecrowd.captainhook.tutorial.testservice;

import com.swipecrowd.captainhook.framework.server.Controller;
import com.swipecrowd.captainhook.tutorial.testservice.server.ServerProperties;

public class ServiceMain {
    public static void main(String[] args) {
        Controller.run(
                new ServerProperties(),
                args);
    }
}
