package com.swipecrowd.captainhook.framework.generation.server;

import com.swipecrowd.captainhook.framework.generation.Generator;
import com.google.common.base.Joiner;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class GenerateServerClasses extends Generator {
    public static void main(String[] args) throws IOException {
        if(args[0].equals("framework-core-server")) {
            return;
        }
        final String serviceName = args[0];
        final String basePackage = args[1];

        new GenerateServerClasses().run(serviceName, basePackage, "server");
    }
}
