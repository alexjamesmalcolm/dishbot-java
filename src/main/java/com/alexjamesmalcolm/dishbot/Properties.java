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
    private final String environment;


    @Autowired
    public Properties(
            @Value("${groupme.accesstoken}") String groupMeAccessToken,
            @Value("${groupme.baseurl}") String baseUrl,
            @Value("${rollbar.accesstoken}") String rollbarAccessToken,
            @Value("${rollbar.endpoint}") String rollbarEndpoint,
            @Value("${environment}") String environment) {
        this.groupMeAccessToken = groupMeAccessToken;
        this.baseUrl = baseUrl;
        this.rollbarAccessToken = rollbarAccessToken;
        this.rollbarEndpoint = rollbarEndpoint;
        this.environment = environment;
    }

    public String getGroupMeAccessToken() {
        return groupMeAccessToken;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getRollbarAccessToken() {
        return rollbarAccessToken;
    }

    public String getRollbarEndpoint() {
        return rollbarEndpoint;
    }
}
