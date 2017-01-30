package nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose;

import lombok.Getter;

import java.util.List;

@Getter
public abstract class AbstractSelfDiagnose {
    public abstract List<SelfDiagnoseItem> getItems();
    public abstract String getVersion();
}
