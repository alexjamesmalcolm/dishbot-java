package com.alexjamesmalcolm.dishbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Properties {

    private final String baseUrl;
    private final String groupMeAccessToken;
    private final String rollbarAccessToken;
    private final String rollbarEndpoint;


    @Autowired
    public Properties(
            @Value("${groupme.accesstoken}") String groupMeAccessToken,
            @Value("${groupme.baseurl}") String baseUrl,
            @Value("${rollbar.accesstoken}") String rollbarAccessToken,
            @Value("${rollbar.endpoint}") String rollbarEndpoint) {
        this.groupMeAccessToken = groupMeAccessToken;
        this.baseUrl = baseUrl;
        this.rollbarAccessToken = rollbarAccessToken;
        this.rollbarEndpoint = rollbarEndpoint;
    }

    public String getGroupMeAccessToken() {
        return groupMeAccessToken;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
