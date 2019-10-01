package com.arthurvlug.captainhook.examplemiddleservice.activity.merge;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import com.arthurvlug.captainhook.framework.common.response.Output;

import java.time.Instant;

@Builder
@Value
@EqualsAndHashCode(callSuper = false)
public class MergeOutput extends Output {
    private String message;
    private Instant respondingTime;
}
