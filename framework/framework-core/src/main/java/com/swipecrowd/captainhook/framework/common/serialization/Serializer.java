package com.swipecrowd.captainhook.framework.common.serialization;

import com.google.gson.reflect.TypeToken;

public abstract class Serializer {
    public abstract <T> byte[] serialize(T value);

    public abstract <T> T deserialize(byte[] jsonBytes, TypeToken<T> clazz);
}
