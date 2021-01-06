package com.swipecrowd.captainhook.framework.application.common.serialization.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;
import java.time.Instant;

public class InstantSerializer implements com.google.gson.JsonSerializer<Instant>, JsonDeserializer<Instant> {
    @Override
    public JsonElement serialize(Instant src, Type typeOfSrc,	JsonSerializationContext context) {
        return new JsonPrimitive(src.toEpochMilli());
    }

    @Override
    public Instant deserialize(JsonElement json, Type typeOfT,  JsonDeserializationContext context) throws JsonParseException {
        final long epochMilli = json.getAsJsonPrimitive().getAsLong();
        return Instant.ofEpochMilli(epochMilli);
    }
}
