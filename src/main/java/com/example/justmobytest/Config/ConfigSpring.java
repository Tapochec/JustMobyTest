package com.example.justmobytest.Config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigSpring {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .build();
    }

}
