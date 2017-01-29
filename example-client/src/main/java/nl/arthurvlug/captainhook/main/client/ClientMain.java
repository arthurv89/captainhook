package nl.arthurvlug.captainhook.main.client;

import nl.arthurvlug.captainhook.exampleservice.client.ExampleServiceClientComponentScanner;
import nl.arthurvlug.captainhook.framework.client.CaptainHookApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ExampleServiceClientComponentScanner.class)
public class ClientMain {
    public static void main(final String[] args) {
        CaptainHookApplication.run(ClientMain.class, args);
    }
}