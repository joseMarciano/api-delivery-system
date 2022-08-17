package com.delivery.system.configs;

import com.delivery.system.configs.json.Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigObjectMapper {

    @Bean
    public ObjectMapper objectMapper() {
        return Json.mapper();
    }
}
