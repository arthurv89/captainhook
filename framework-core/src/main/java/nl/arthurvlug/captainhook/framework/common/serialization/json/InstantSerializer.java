package nl.arthurvlug.captainhook.framework.common.serialization.json;

import com.google.gson.*;

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
