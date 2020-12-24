package com.swipecrowd.captainhook.framework.generation;

import java.util.Set;

public interface Replacer {
    String replace(String contents, String servicePackage, String serviceName, Set<String> activities);
}
