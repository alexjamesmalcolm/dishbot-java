package com.alexjamesmalcolm.dishbot;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DishbotConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        builder.interceptors(new Interceptor());
        return builder.build();
    }
}
