package com.swipecrowd.captainhook.framework.generation;

import java.util.Properties;
import java.util.Set;

public interface Replacer {
    String replace(String contents, String basePackage, String serviceName, Set<String> activities, String newHost, String newPort);
}
