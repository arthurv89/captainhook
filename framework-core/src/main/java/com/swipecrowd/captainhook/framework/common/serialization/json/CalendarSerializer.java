package com.swipecrowd.captainhook.framework.common.serialization.json;

import com.google.gson.*;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Calendar;

public class CalendarSerializer implements JsonSerializer<Calendar>, JsonDeserializer<Calendar> {
    @Override
    public JsonElement serialize(Calendar src, Type typeOfSrc,	JsonSerializationContext context) {
        return new JsonPrimitive(src.getTimeInMillis());
    }

    @Override
    public Calendar deserialize(JsonElement json, Type typeOfT,  JsonDeserializationContext context) throws JsonParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(json.getAsJsonPrimitive().getAsLong());
        return cal;
    }
}
