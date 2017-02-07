package nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class SelfDiagnoseItem {
    private final String key;
    private final SelfDiagnoseItemValue value;

    public SelfDiagnoseItem(String key, String value) {
        this(key, new SelfDiagnoseItemValue.SuccessValue(value));
    }
}
