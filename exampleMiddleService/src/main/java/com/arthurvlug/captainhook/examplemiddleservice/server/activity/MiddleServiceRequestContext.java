package com.arthurvlug.captainhook.examplemiddleservice.server.activity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import com.arthurvlug.captainhook.framework.server.DefaultRequestContext;

@EqualsAndHashCode(callSuper = true)
@Value
@AllArgsConstructor
public class MiddleServiceRequestContext extends DefaultRequestContext {
    final Long startingTime;
}
