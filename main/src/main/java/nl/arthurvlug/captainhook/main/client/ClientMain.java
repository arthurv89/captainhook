package nl.arthurvlug.captainhook.main.client;

import nl.arthurvlug.captainhook.framework.client.ClientApplication;
import org.springframework.boot.SpringApplication;

public class ClientMain {
    public static void main(final String[] args) {
        SpringApplication.run(new Class[]{
                com.arthurvlug.captainhook.examplemiddleservice.client.ClientSpringComponents.class,
                ClientApplication.class,
                ClientRunner.class
        }, args);
    }
}