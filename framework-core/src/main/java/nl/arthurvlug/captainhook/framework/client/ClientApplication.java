package nl.arthurvlug.captainhook.framework.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableAutoConfiguration
public class ClientApplication {
    @Autowired
    AbstractClientRunner abstractClientRunner;

    @PostConstruct
    private void postConstruct() {
        abstractClientRunner.run();
    }
}
