package com.arthurvlug.captainhook.tutorial.hellomoonservice.activity.hellomoon;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import com.arthurvlug.captainhook.framework.common.response.Output;

import java.time.Instant;

@Builder
@Value
@EqualsAndHashCode(callSuper = false)
public class HelloMoonOutput extends Output {
    private String message;
    private Instant respondingTime;
}
