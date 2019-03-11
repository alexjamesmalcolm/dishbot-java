package com.alexjamesmalcolm.dishbot;

import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.ConfigBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Configuration
public class DishbotConfiguration {

    @Resource
    Properties properties;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        builder.interceptors(new Interceptor());
        return builder.build();
    }

    @Bean
    public Rollbar rollbar() {
        String accessToken = properties.getRollbarAccessToken();
        String environment = properties.getEnvironment();
        return new Rollbar(ConfigBuilder.withAccessToken(accessToken).environment(environment).handleUncaughtErrors(true).build());
    }
}
