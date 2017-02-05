package nl.arthurvlug.captainhook.main.client;

import nl.arthurvlug.captainhook.exampleservice.client.ExampleserviceClientComponentScanner;
import nl.arthurvlug.captainhook.exampleservice2.client.Exampleservice2ClientComponentScanner;
import nl.arthurvlug.captainhook.framework.client.CaptainHookApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
        ExampleserviceClientComponentScanner.class,
        Exampleservice2ClientComponentScanner.class
})
public class ClientMain {
    public static void main(final String[] args) {
        CaptainHookApplication.run(ClientMain.class, args);
    }
}