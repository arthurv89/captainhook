package nl.arthurvlug.captainhook.main.client;

import com.arthurvlug.captainhook.examplemiddleservice.clientlib.ClientWrapperProvider;
import org.springframework.boot.SpringApplication;

public class ClientMain {
    public static void main(final String[] args) {
        SpringApplication.run(new Class[]{
                ClientWrapperProvider.class,
                ClientRunner.class
        }, args);
    }
}