package com.alexjamesmalcolm.dishbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class Properties {

    private final URI baseUrl;
    private final String rollbarAccessToken;
    private final URI rollbarEndpoint;
    private final String environment;
    private final URI dishbotUrl;


    @Autowired
    public Properties(
            @Value("${groupme.baseurl}") String baseUrl,
            @Value("${rollbar.accesstoken}") String rollbarAccessToken,
            @Value("${rollbar.endpoint}") String rollbarEndpoint,
            @Value("${environment}") String environment,
            @Value("${dishbot.callback}") String dishbotUrl) {
        this.baseUrl = URI.create(baseUrl);
        this.rollbarAccessToken = rollbarAccessToken;
        this.rollbarEndpoint = URI.create(rollbarEndpoint);
        this.environment = environment;
        this.dishbotUrl = URI.create(dishbotUrl);
    }

    public URI getBaseUrl() {
        return baseUrl;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getRollbarAccessToken() {
        return rollbarAccessToken;
    }

    public URI getRollbarEndpoint() {
        return rollbarEndpoint;
    }

    public URI getDishbotCallbackUrl() {
        return dishbotUrl;
    }
}
