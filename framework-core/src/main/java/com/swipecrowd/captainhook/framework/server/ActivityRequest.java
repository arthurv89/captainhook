package com.swipecrowd.captainhook.framework.server;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpSession;

@AllArgsConstructor
@Getter
public class ActivityRequest<I extends Input> {
    private final I input;
    private final HttpSession session;
}
