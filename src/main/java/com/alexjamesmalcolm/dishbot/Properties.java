package com.alexjamesmalcolm.dishbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Properties {

    private final String baseUrl;
    private final String accessToken;

    @Autowired
    public Properties(@Value("${groupme.accesstoken}") String accessToken, @Value("${groupme.baseurl}") String baseUrl) {
        this.accessToken = accessToken;
        this.baseUrl = baseUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
