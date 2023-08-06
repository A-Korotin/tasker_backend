package com.korotin.tasker.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class JsonUtils {

    private static final ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    private JsonUtils() {}

    public static String asJsonString(Object o) throws Exception {
        return mapper.writeValueAsString(o);
    }

    public static  <T> T fromJson(String json, Class<T> clazz) throws Exception {
        return mapper.readValue(json, clazz);
    }

    public static  <T> T fromJson(String json, TypeReference<T> type) throws Exception {
        return mapper.readValue(json, type);
    }
}
