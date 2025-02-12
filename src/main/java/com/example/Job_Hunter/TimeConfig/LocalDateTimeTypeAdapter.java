package com.example.Job_Hunter.TimeConfig;

import com.nimbusds.jose.shaded.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeTypeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        return LocalDateTime.parse(jsonElement.getAsString(), formatter);
    }

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(localDateTime.format(formatter));
    }
}
