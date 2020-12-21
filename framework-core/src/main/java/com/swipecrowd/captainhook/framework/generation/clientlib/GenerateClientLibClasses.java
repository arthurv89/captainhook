package com.swipecrowd.captainhook.framework.generation.clientlib;

import com.swipecrowd.captainhook.framework.generation.Generator;

import java.io.IOException;
import java.util.Arrays;

public class GenerateClientLibClasses extends Generator {
    public static void main(String[] args) throws IOException {
        if(args[0].equals("framework-core-clientlib")) {
            // Skip if the framework triggered this.
            return;
        }

        System.out.println(Arrays.toString(args));
        final String serviceName = args[0].replace("-clientlib", "");
        final String basePackage = args[1];

        new GenerateClientLibClasses().run(serviceName, basePackage, "client");
    }
}
