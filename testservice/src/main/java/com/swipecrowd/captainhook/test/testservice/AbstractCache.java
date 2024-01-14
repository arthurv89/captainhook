package com.swipecrowd.captainhook.test.testservice;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.swipecrowd.captainhook.framework.application.common.Output;
import com.swipecrowd.captainhook.framework.application.common.Input;

import java.util.Optional;

public class AbstractCache<I extends Input, O extends Output> {
    protected static final int MAXIMUM_SIZE = 1000;

    protected final Cache<I, O> cache;

    AbstractCache() {
        cache = createCache();
    }

    protected Cache<I, O> createCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(MAXIMUM_SIZE)
                .build();
    }

    public void put(final I input, final O output) {
        cache.put(input, output);
    }

    public Optional<O> get(final I input) {
        return Optional.ofNullable(cache.getIfPresent(input));
    }
}
