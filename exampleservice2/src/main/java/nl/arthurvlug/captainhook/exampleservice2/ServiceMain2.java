package nl.arthurvlug.captainhook.exampleservice2;

import nl.arthurvlug.captainhook.exampleservice2.server.ServerSpringComponents;
import nl.arthurvlug.captainhook.exampleservice2.server.activity.helloworld.HelloWorldActivity;
import nl.arthurvlug.captainhook.exampleservice2.server.activity.ping.PingActivity;
import nl.arthurvlug.captainhook.framework.server.Controller;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@EnableAutoConfiguration
public class ServiceMain2 {
    public static void main(String[] args) {
        SpringApplication.run(new Class[]{
                ServiceMain2.class,
                ServerSpringComponents.class,
                HelloWorldActivity.class,
                PingActivity.class,
                Controller.class
        }, args);
    }
}
