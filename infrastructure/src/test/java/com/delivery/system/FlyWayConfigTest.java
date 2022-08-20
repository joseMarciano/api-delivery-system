package com.delivery.system;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class FlyWayConfigTest {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public Flyway flyway() {
        FluentConfiguration fluentConfiguration = new FluentConfiguration();
        fluentConfiguration
                .dataSource(this.url, this.username, this.password)
                .cleanDisabled(false);
        return new Flyway(fluentConfiguration);
    }
}
