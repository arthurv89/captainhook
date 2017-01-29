package nl.arthurvlug.captainhook.framework.common.serialization.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import nl.arthurvlug.captainhook.framework.common.serialization.Serializer;

import java.util.Calendar;

public class JsonSerializer extends Serializer {
    private final Gson gson = new GsonBuilder()
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
