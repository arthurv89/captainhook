package com.swipecrowd.captainhook.framework.generation.clientlib;

import com.swipecrowd.captainhook.framework.generation.Generator;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class GenerateClientLibClasses extends Generator {
    public static void main(String[] args) throws IOException {
        if(args[0].equals("framework-core-clientlib")) {
            // Skip if the framework triggered this.
            return;
        }

        final String serviceName = args[0].replace("-clientlib", "");
        final String basePackage = args[1];

        new GenerateClientLibClasses().run(serviceName, basePackage, "client");
    }
}
