package nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose;

import lombok.Value;

@Value
public class SelfDiagnoseItem {
    private final String key;
    private final String value;
}
