package ru.practicum.ewm.config;


import client.StatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${stats-server-url}")
    private String serverUrl;

    @Bean
    public RestTemplateBuilder getBuilder() {
        return new RestTemplateBuilder();
    }

    @Bean
    public StatClient getStatClient(RestTemplateBuilder builder) {
        return new StatClient(serverUrl, builder);
    }
}
