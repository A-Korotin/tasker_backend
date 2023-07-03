package com.korotin.tasker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseTests {
    protected ObjectMapper mapper = new ObjectMapper();

    protected String asJsonString(Object o) throws Exception {
        return mapper.writeValueAsString(o);
    }

    protected <T> T fromJson(String json, Class<T> clazz) throws Exception {
        return mapper.readValue(json, clazz);
    }

    protected <T> T fromJson(String json, TypeReference<T> type) throws Exception {
        return mapper.readValue(json, type);
    }
}