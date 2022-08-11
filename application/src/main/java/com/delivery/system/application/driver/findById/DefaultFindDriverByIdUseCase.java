package com.delivery.system.application.driver.findById;

import com.delivery.system.domain.driver.Driver;
import com.delivery.system.domain.driver.DriverGateway;
import com.delivery.system.domain.driver.DriverID;
import com.delivery.system.domain.exceptions.NotFoundException;

import java.util.function.Supplier;

public class DefaultFindDriverByIdUseCase extends FindDriverByIdUseCase {

    private final DriverGateway driverGateway;

    public DefaultFindDriverByIdUseCase(DriverGateway driverGateway) {
        this.driverGateway = driverGateway;
    }

    @Override
    public FindDriverByIdOutput execute(FindDriverByIdCommand aCommand) {
        final var anId = DriverID.from(aCommand.anId());
        return driverGateway.findById(anId)
                .map(FindDriverByIdOutput::from)
                .orElseThrow(notFound(anId));
    }

    private Supplier<NotFoundException> notFound(DriverID anId) {
        return () -> NotFoundException.with(anId, Driver.class);
    }

}
