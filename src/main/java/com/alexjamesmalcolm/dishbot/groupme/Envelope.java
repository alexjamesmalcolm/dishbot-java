package com.alexjamesmalcolm.dishbot.groupme;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Envelope {

    private Map response;
    private HttpStatus code;
    private List<String> errors;

    @JsonCreator
    private Envelope(
            @JsonProperty("response") Map response,
            @JsonProperty("meta") Map meta
    ) {
        this.response = response;
        this.code = HttpStatus.resolve((Integer) meta.get("code"));
        this.errors = Optional.ofNullable((String[]) meta.get("errors")).map(Arrays::asList).orElse(null);
    }

    public <T> T getResponse(Class<T> typeToResolveTo) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(response, typeToResolveTo);
    }
}
