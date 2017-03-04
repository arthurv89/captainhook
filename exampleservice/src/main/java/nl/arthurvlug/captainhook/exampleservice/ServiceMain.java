package nl.arthurvlug.captainhook.exampleservice;

import nl.arthurvlug.captainhook.exampleservice.server.ServerSpringComponentsImporter;
import nl.arthurvlug.captainhook.framework.server.spring.CaptainHookSpringController;

public class ServiceMain {
    public static void main(String[] args) {
        CaptainHookSpringController.run(ServerSpringComponentsImporter.class, args);
    }
}
