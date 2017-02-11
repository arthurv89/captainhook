package com.arthurvlug.captainhook.examplemiddleservice;

import com.arthurvlug.captainhook.examplemiddleservice.server.ServerSpringComponents;
import com.arthurvlug.captainhook.examplemiddleservice.server.activity.merge.MergeActivity;
import nl.arthurvlug.captainhook.framework.server.Controller;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

@EnableAutoConfiguration
@Import({
        ServerSpringComponents.class,
        nl.arthurvlug.captainhook.exampleservice.client.ClientSpringComponents.class,
        nl.arthurvlug.captainhook.exampleservice2.client.ClientSpringComponents.class,
        MergeActivity.class,
        Controller.class
})
public class MiddleServiceMain {
    public static void main(String[] args) {
        final Class[] newClasses = new Class[] {
                MiddleServiceMain.class,
                Controller.class
        };
        SpringApplication.run(newClasses, args);
    }
}
