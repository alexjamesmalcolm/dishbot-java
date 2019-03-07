package com.alexjamesmalcolm.dishbot.groupme;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class Envelope {

    private Map response;
    private Map meta;

    public void setResponse(Map response) {
        this.response = response;
    }

    public void setMeta(Map meta) {
        this.meta = meta;
    }

    public <T> T getResponse(Class<T> typeToResolveTo) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(response, typeToResolveTo);
    }
}
