package com.delivery.system.application.driver.deleteById;

import com.delivery.system.domain.driver.DriverGateway;
import com.delivery.system.domain.driver.DriverID;

import static java.util.Objects.isNull;

public class DefaultDeleteDriverByIdUseCase extends DeleteDriverByIdUseCase {


    private final DriverGateway driverGateway;

    public DefaultDeleteDriverByIdUseCase(DriverGateway driverGateway) {
        this.driverGateway = driverGateway;
    }

    @Override
    public void execute(DeleteDriverByIdCommand anIn) {
        if (isNull(anIn.id())) return;

        driverGateway.deleteById(DriverID.from(anIn.id()));
    }
}
