package com.delivery.system.application.driver.create;

import com.delivery.system.domain.driver.Driver;
import com.delivery.system.domain.driver.DriverGateway;

import java.util.Objects;

public class DefaultCreateDriverUseCase extends CreateDriverUseCase {

    private final DriverGateway createDriverGateway;

    public DefaultCreateDriverUseCase(DriverGateway createDriverGateway) {
        this.createDriverGateway = Objects.requireNonNull(createDriverGateway, "createDriverGateway must not be null.");
    }

    @Override
    public CreateDriverOutput execute(CreateDriverCommand anIn) {
        final var driver = Driver.newDriver(anIn.name());
        return CreateDriverOutput.from(createDriverGateway.create(driver));
    }

}
