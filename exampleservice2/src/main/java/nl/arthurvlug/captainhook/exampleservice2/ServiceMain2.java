package nl.arthurvlug.captainhook.exampleservice2;

import nl.arthurvlug.captainhook.exampleservice2.server.ServerSpringComponentsImporter;
import nl.arthurvlug.captainhook.framework.server.spring.CaptainHookSpringController;

public class ServiceMain2 {
    public static void main(String[] args) {
        CaptainHookSpringController.run(ServerSpringComponentsImporter.class, args);
    }
}
