package nl.arthurvlug.captainhook.main.client;

import com.arthurvlug.captainhook.examplemiddleservice.clientlib.ClientWrapperSpringComponents;
import org.springframework.boot.SpringApplication;

public class ClientMain {
    public static void main(final String[] args) {
        SpringApplication.run(new Class[]{
                ClientWrapperSpringComponents.class,
                ClientRunner.class
        }, args);
    }
}