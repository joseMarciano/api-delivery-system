package com.delivery.system.configs.driver;


import com.delivery.system.application.driver.create.CreateDriverUseCase;
import com.delivery.system.application.driver.create.DefaultCreateDriverUseCase;
import com.delivery.system.application.driver.deleteById.DefaultDeleteDriverByIdUseCase;
import com.delivery.system.application.driver.deleteById.DeleteDriverByIdUseCase;
import com.delivery.system.application.driver.findById.DefaultFindDriverByIdUseCase;
import com.delivery.system.application.driver.findById.FindDriverByIdUseCase;
import com.delivery.system.application.driver.retrieve.listAll.DefaultListAllDriverUseCase;
import com.delivery.system.application.driver.retrieve.listAll.ListAllDriverUseCase;
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

    @Bean
    public DeleteDriverByIdUseCase deleteDriverByIdUseCase() {
        return new DefaultDeleteDriverByIdUseCase(driverGateway);
    }

    @Bean
    public FindDriverByIdUseCase findDriverByIdUseCase() {
        return new DefaultFindDriverByIdUseCase(driverGateway);
    }

    @Bean
    public ListAllDriverUseCase listAllDriverUseCase() {
        return new DefaultListAllDriverUseCase(driverGateway);
    }
}
