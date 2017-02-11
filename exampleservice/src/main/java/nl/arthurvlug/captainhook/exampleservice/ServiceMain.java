package nl.arthurvlug.captainhook.exampleservice;

import nl.arthurvlug.captainhook.exampleservice.server.ServerSpringComponents;
import nl.arthurvlug.captainhook.exampleservice.server.activity.helloworld.HelloWorldActivity;
import nl.arthurvlug.captainhook.framework.server.Controller;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@EnableAutoConfiguration
public class ServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(new Class[] {
                ServiceMain.class,
                ServerSpringComponents.class,
                HelloWorldActivity.class,
                Controller.class
        }, args);
    }
}
