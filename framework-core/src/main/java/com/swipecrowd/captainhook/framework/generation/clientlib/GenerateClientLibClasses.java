package com.swipecrowd.captainhook.framework.generation.clientlib;

import com.swipecrowd.captainhook.framework.generation.Generator;

import java.io.IOException;
import java.util.Arrays;

public class GenerateClientLibClasses extends Generator {
    public static void main(String[] args) throws IOException {
        System.out.println(Arrays.toString(args));
        if(args.length != 1) {
            throw new RuntimeException("Could not run: Number of arguments incorrect");
        }
        if(args[0].equals("com.swipecrowd.captainhook")) {
            // Skip if the framework triggered this.
            System.out.println("Skip!");
            return;
        }

        final String servicePackage = args[0];

        new GenerateClientLibClasses().run(servicePackage, "client");
    }
}
