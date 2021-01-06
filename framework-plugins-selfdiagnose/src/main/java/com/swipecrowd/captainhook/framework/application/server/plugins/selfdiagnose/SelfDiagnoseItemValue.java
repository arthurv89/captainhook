package com.swipecrowd.captainhook.framework.application.server.plugins.selfdiagnose;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SelfDiagnoseItemValue {
    private final boolean success;
    private final String message;

    public SelfDiagnoseItemValue(boolean success, String successMessage, String failureMessage) {
        this.success = success;
        this.message = success ? successMessage : failureMessage;
    }

    public static class SuccessValue extends SelfDiagnoseItemValue {
        public SuccessValue(final String message) {
            super(true, message);
        }
    }

    public static class FailureValue extends SelfDiagnoseItemValue {
        public FailureValue(final String message) {
            super(false, message);
        }
    }
}

