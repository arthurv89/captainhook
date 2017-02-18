package nl.arthurvlug.captainhook.exampleservice;

import nl.arthurvlug.captainhook.exampleservice.server.ServerSpringComponentsImporter;
import nl.arthurvlug.captainhook.framework.server.Controller;

public class ServiceMain {
    public static void main(String[] args) {
        Controller.run(ServerSpringComponentsImporter.class, args);
    }
}
