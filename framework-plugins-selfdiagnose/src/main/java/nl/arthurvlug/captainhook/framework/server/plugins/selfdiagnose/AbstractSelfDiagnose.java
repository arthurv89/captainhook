package nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose;

import lombok.Getter;

import java.util.Calendar;
import java.util.List;

@Getter
public abstract class AbstractSelfDiagnose {
    public SelfDiagnoseLayout getLayout() {
        return new SelfDiagnoseLayout("#4CAF50");
    }

    public abstract String getName();
    public abstract List<SelfDiagnoseItem> getItems();
    public abstract String getVersion();
    public final Calendar time = Calendar.getInstance();
}
