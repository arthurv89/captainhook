package com.arthurvlug.captainhook.framework.server;

import java.util.List;

public abstract class Plugin {
    public abstract List<Class<?>> getClasses();
}