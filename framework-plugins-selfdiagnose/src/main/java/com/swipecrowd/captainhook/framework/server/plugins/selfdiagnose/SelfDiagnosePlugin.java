package com.swipecrowd.captainhook.framework.server.plugins.selfdiagnose;

import com.swipecrowd.captainhook.framework.server.Plugin;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SelfDiagnosePlugin extends Plugin {
    private final Class<? extends AbstractSelfDiagnose> selfDiagnoseClass;

    @Override
    public ImmutableList<Class<?>> getClasses() {
        return ImmutableList.of(
                selfDiagnoseClass,
                SelfDiagnoseController.class,
                SelfDiagnosePluginConfiguration.class);
    }
}
