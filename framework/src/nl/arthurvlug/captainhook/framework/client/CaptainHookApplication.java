package nl.arthurvlug.captainhook.framework.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;

@ComponentScan(basePackages = {
        "nl.arthurvlug.captainhook.framework.client",
        "nl.arthurvlug.captainhook.framework.common"
})
public class CaptainHookApplication {
    @Autowired
    AbstractClientRunner abstractClientRunner;

    @PostConstruct
    private void postConstruct() {
        abstractClientRunner.run();
    }

    public static void run(final Class<?> clazz, final String[] args) {
        SpringApplication.run(new Object[] {CaptainHookApplication.class, clazz}, args);
    }
}
