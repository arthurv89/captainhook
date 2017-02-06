package nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DefaultSelfDiagnose extends AbstractSelfDiagnose {
    private final String name = "UNKNOWN";
    private final List<SelfDiagnoseItem> items = new ArrayList<>();
    private final String version = "UNKNOWN";
}
