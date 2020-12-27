package com.swipecrowd.captainhook.framework.generation;

import java.io.File;
import java.util.Set;

public interface Replacer {
    String replace(String contents, String servicePackage, String serviceName, Set<String> activities);

    File createRenamedFile(String serviceName, final File file);
}
