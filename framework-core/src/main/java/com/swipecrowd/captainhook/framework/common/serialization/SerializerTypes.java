package com.swipecrowd.captainhook.framework.common.serialization;

import com.swipecrowd.captainhook.framework.common.serialization.json.JsonSerializer;

public enum SerializerTypes {
    JSON(new JsonSerializer());

    private final Serializer serializer;

    <S extends Serializer> SerializerTypes(final S serializer) {
            this.serializer = serializer;
        }

    public static Serializer getByName(final String encoding) {
        return SerializerTypes.valueOf(encoding).serializer;
    }

    public Serializer getSerializer() {
        return getByName(this.name());
    }
}
