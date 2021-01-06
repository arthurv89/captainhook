package com.swipecrowd.captainhook.framework.application.common.serialization.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.swipecrowd.captainhook.framework.application.common.serialization.Serializer;

import java.time.Instant;
import java.util.Calendar;

public class JsonSerializer extends Serializer {
    private final Gson gson = new GsonBuilder()
        .registerTypeHierarchyAdapter(Instant.class, new InstantSerializer())
        .registerTypeHierarchyAdapter(Calendar.class, new CalendarSerializer())
        .create();

    @Override
    public <T> byte[] serialize(T value) {
        return gson.toJson(value).getBytes();
    }

    @Override
    public <T> T deserialize(byte[] jsonBytes, TypeToken<T> type) {
        return gson.fromJson(new String(jsonBytes), type.getType());
    }
}
