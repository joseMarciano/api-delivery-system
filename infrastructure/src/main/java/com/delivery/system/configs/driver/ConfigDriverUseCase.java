package com.delivery.system.configs.driver;


import com.delivery.system.application.driver.create.CreateDriverUseCase;
import com.delivery.system.application.driver.create.DefaultCreateDriverUseCase;
import com.delivery.system.domain.driver.DriverGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigDriverUseCase {

    private final DriverGateway driverGateway;

    public ConfigDriverUseCase(DriverGateway driverGateway) {
        this.driverGateway = driverGateway;
    }

    @Bean
    public CreateDriverUseCase createDriverUseCase() {
        return new DefaultCreateDriverUseCase(driverGateway);
    }
}
