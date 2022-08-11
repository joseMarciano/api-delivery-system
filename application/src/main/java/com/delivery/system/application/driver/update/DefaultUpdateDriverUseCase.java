package com.delivery.system.application.driver.update;

import com.delivery.system.domain.driver.Driver;
import com.delivery.system.domain.driver.DriverGateway;
import com.delivery.system.domain.driver.DriverID;
import com.delivery.system.domain.exceptions.NotFoundException;

import java.util.function.Supplier;

public class DefaultUpdateDriverUseCase extends UpdateDriverUseCase {

    private final DriverGateway driverGateway;

    public DefaultUpdateDriverUseCase(DriverGateway driverGateway) {
        this.driverGateway = driverGateway;
    }

    @Override
    public UpdateDriverOutput execute(UpdateDriverCommand aCommand) {
        final var anId = DriverID.from(aCommand.id());
        final var aName = aCommand.name();

        final var aDriver = driverGateway.findById(anId)
                .orElseThrow(notFound(anId));

        aDriver.update(aName);


        return UpdateDriverOutput.from(driverGateway.update(aDriver));
    }

    private Supplier<NotFoundException> notFound(DriverID anId) {
        return () -> NotFoundException.with(anId, Driver.class);
    }
}
